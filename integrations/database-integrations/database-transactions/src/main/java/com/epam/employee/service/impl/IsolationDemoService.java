package com.epam.employee.service.impl;

import com.epam.employee.entity.Department;
import com.epam.employee.entity.Employee;
import com.epam.employee.repo.DepartmentRepo;
import com.epam.employee.repo.EmployeeRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * IsolationDemoService — ISOLATION LEVELS & CONCURRENCY PROBLEMS
 * ═══════════════════════════════════════════════════════════════════════════════
 * <p>
 * ╔══════════════════════╦════════════╦══════════════════╦═══════════════╗
 * ║  Isolation Level     ║ Dirty Read ║ Non-Repeatable   ║ Phantom Read  ║
 * ║                      ║            ║ Read             ║               ║
 * ╠══════════════════════╬════════════╬══════════════════╬═══════════════╣
 * ║ READ_UNCOMMITTED     ║  Possible  ║  Possible        ║  Possible     ║
 * ║ READ_COMMITTED       ║  Prevented ║  Possible        ║  Possible     ║
 * ║ REPEATABLE_READ      ║  Prevented ║  Prevented       ║  Possible*    ║
 * ║ SERIALIZABLE         ║  Prevented ║  Prevented       ║  Prevented    ║
 * ╚══════════════════════╩════════════╩══════════════════╩═══════════════╝
 * <p>
 * (*) PostgreSQL's REPEATABLE_READ actually prevents phantom reads too,
 * because it uses Snapshot Isolation (MVCC). This is an implementation
 * detail — the SQL standard says phantoms are possible at this level.
 * <p>
 * ─────────────────────────────────────────────────────────────────────────
 * PROBLEM DEFINITIONS
 * ─────────────────────────────────────────────────────────────────────────
 * <p>
 * 1. DIRTY READ
 * Transaction B reads data that Transaction A has modified but NOT yet
 * committed. If A rolls back, B has read "dirty" (non-existent) data.
 * <p>
 * Example: A updates employee salary from 5000 → 9000 (not committed).
 * B reads salary = 9000. A rolls back. B used wrong data.
 * <p>
 * Solution: Use READ_COMMITTED or higher isolation level.
 * <p>
 * 2. NON-REPEATABLE READ
 * Transaction A reads a row, Transaction B modifies and commits that row,
 * then A reads it again — and gets a DIFFERENT value.
 * <p>
 * Example: A reads employee salary = 5000.
 * B updates salary to 7000 and commits.
 * A reads again — salary = 7000. The read is NOT repeatable.
 * <p>
 * Solution: Use REPEATABLE_READ or SERIALIZABLE isolation level.
 * <p>
 * 3. PHANTOM READ
 * Transaction A runs a range query (e.g., "all employees in dept 1").
 * Transaction B inserts/deletes a row matching A's query and commits.
 * A runs the same query again — different set of rows appears ("phantom").
 * <p>
 * Example: A counts employees in dept 1 → finds 5.
 * B inserts a new employee in dept 1 and commits.
 * A counts again → finds 6. The extra row is a "phantom".
 * <p>
 * Solution: Use SERIALIZABLE isolation level.
 * <p>
 * 4. LOST UPDATE
 * Two transactions read the same row, both modify it based on the
 * original value, and the second commit OVERWRITES the first.
 * <p>
 * Example: Both A and B read salary = 5000.
 * A sets salary = 5000 + 1000 = 6000, commits.
 * B sets salary = 5000 + 2000 = 7000, commits.
 * A's raise is LOST — final salary is 7000, not 8000.
 * <p>
 * Solution: Use @Version (optimistic locking) or SELECT FOR UPDATE
 * (pessimistic locking).
 * <p>
 * ─────────────────────────────────────────────────────────────────────────
 * PostgreSQL CAVEAT
 * ─────────────────────────────────────────────────────────────────────────
 * PostgreSQL does NOT support true READ_UNCOMMITTED — it silently
 * upgrades it to READ_COMMITTED. To observe actual dirty reads, you
 * would need MySQL/MariaDB with the MyISAM or InnoDB engine configured
 * for READ UNCOMMITTED.
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Service
public class IsolationDemoService {

    private static final Logger log = LoggerFactory.getLogger(IsolationDemoService.class);

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public IsolationDemoService(EmployeeRepo employeeRepo,
                                DepartmentRepo departmentRepo,
                                EntityManager entityManager,
                                TransactionTemplate transactionTemplate) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 1. DIRTY READ DEMO
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * DIRTY READ — READ_UNCOMMITTED isolation
     * ────────────────────────────────────────────────────────────────────
     * WHAT HAPPENS:
     * Thread-1 (Tx-A): Updates employee salary from 5000 → 9999 but does
     * NOT commit yet (simulates long processing).
     * Thread-2 (Tx-B): Reads the SAME employee with READ_UNCOMMITTED.
     * In theory, Tx-B should see the uncommitted 9999.
     * Thread-1 (Tx-A): Rolls back.
     * Result:           Tx-B read data that NEVER existed in the DB.
     * <p>
     * POSTGRESQL NOTE:
     * PostgreSQL silently upgrades READ_UNCOMMITTED → READ_COMMITTED,
     * so Tx-B will NOT see the dirty value. The demo logs this.
     * On MySQL with READ UNCOMMITTED, you would see the dirty read.
     * <p>
     * HOW TO FIX:
     * Use READ_COMMITTED or higher. PostgreSQL does this automatically.
     * ────────────────────────────────────────────────────────────────────
     */
    public Map<String, String> demoDirtyRead(int empId) {
        Map<String, String> result = new LinkedHashMap<>();
        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee " + empId + " not found. Create one first.");
            return result;
        }

        BigDecimal originalSalary = emp.getSalary();
        result.put("1_originalSalary", originalSalary.toString());

        CountDownLatch writerReady = new CountDownLatch(1);
        CountDownLatch readerDone = new CountDownLatch(1);

        // Thread-1: Writer — updates salary but does NOT commit
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> writerFuture = executor.submit(() -> {
            transactionTemplate.execute(status -> {
                Employee e = employeeRepo.findById(empId).orElseThrow();
                e.setSalary(new BigDecimal("9999"));
                employeeRepo.saveAndFlush(e);
                log.info("[DIRTY-READ Writer] Flushed salary=9999 (NOT committed yet)");
                writerReady.countDown();
                try {
                    readerDone.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                // ROLLBACK — the salary change was never real
                status.setRollbackOnly();
                log.info("[DIRTY-READ Writer] Rolling back");
                return null;
            });
        });

        // Thread-2: Reader — reads with READ_UNCOMMITTED
        Future<BigDecimal> readerFuture = executor.submit(() -> {
            try {
                writerReady.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            TransactionTemplate readTx = new TransactionTemplate(transactionTemplate.getTransactionManager());
            readTx.setIsolationLevel(TransactionTemplate.ISOLATION_READ_UNCOMMITTED);
            return readTx.execute(status -> {
                Employee e = employeeRepo.findById(empId).orElseThrow();
                log.info("[DIRTY-READ Reader] Read salary={} with READ_UNCOMMITTED", e.getSalary());
                readerDone.countDown();
                return e.getSalary();
            });
        });

        try {
            writerFuture.get(10, TimeUnit.SECONDS);
            BigDecimal readValue = readerFuture.get(10, TimeUnit.SECONDS);
            result.put("2_readerSawSalary", readValue.toString());
            if (readValue.compareTo(new BigDecimal("9999")) == 0) {
                result.put("3_dirtyReadOccurred", "YES — reader saw uncommitted data!");
            } else {
                result.put("3_dirtyReadOccurred", "NO — PostgreSQL upgrades READ_UNCOMMITTED to READ_COMMITTED");
            }
        } catch (Exception ex) {
            result.put("error", ex.getMessage());
        } finally {
            executor.shutdown();
        }

        // Verify salary is back to original
        BigDecimal finalSalary = employeeRepo.findById(empId).map(Employee::getSalary).orElse(BigDecimal.ZERO);
        result.put("4_finalSalary", finalSalary.toString());
        result.put("5_lesson", "READ_UNCOMMITTED allows dirty reads. PostgreSQL prevents them automatically " +
                "by upgrading to READ_COMMITTED. Use READ_COMMITTED or higher to be safe.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. NON-REPEATABLE READ DEMO
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * NON-REPEATABLE READ — READ_COMMITTED isolation
     * ────────────────────────────────────────────────────────────────────
     * WHAT HAPPENS:
     * Tx-A (READ_COMMITTED): Reads department budget → 100000
     * Tx-B: Updates budget to 200000 and COMMITS.
     * Tx-A: Reads the SAME department again → 200000 (DIFFERENT!)
     * <p>
     * The same query in the same transaction returns a different value
     * because READ_COMMITTED re-evaluates each statement against the
     * latest committed data.
     * <p>
     * HOW TO FIX:
     * Use REPEATABLE_READ — each read within the same transaction
     * returns a consistent snapshot taken at transaction start.
     * ────────────────────────────────────────────────────────────────────
     */
    public Map<String, String> demoNonRepeatableRead(int deptId) {
        Map<String, String> result = new LinkedHashMap<>();
        Department dept = departmentRepo.findById(deptId).orElse(null);
        if (dept == null) {
            result.put("error", "Department " + deptId + " not found. Create one first.");
            return result;
        }

        BigDecimal originalBudget = dept.getBudget();
        result.put("1_originalBudget", String.valueOf(originalBudget));

        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch updateCommitted = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Tx-A: Reader with READ_COMMITTED — reads twice
        Future<List<BigDecimal>> readerFuture = executor.submit(() -> {
            TransactionTemplate readTx = new TransactionTemplate(transactionTemplate.getTransactionManager());
            readTx.setIsolationLevel(TransactionTemplate.ISOLATION_READ_COMMITTED);
            return readTx.execute(status -> {
                List<BigDecimal> readings = new ArrayList<>();

                // First read
                Department d = departmentRepo.findById(deptId).orElseThrow();
                readings.add(d.getBudget());
                log.info("[NON-REPEATABLE Reader] 1st read: budget={}", d.getBudget());
                firstReadDone.countDown();

                // Wait for writer to commit
                try {
                    updateCommitted.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }

                // Second read — entityManager cache must be cleared to see DB changes
                entityManager.clear();
                Department d2 = departmentRepo.findById(deptId).orElseThrow();
                readings.add(d2.getBudget());
                log.info("[NON-REPEATABLE Reader] 2nd read: budget={}", d2.getBudget());

                return readings;
            });
        });

        // Tx-B: Writer — changes budget and commits
        Future<?> writerFuture = executor.submit(() -> {
            try {
                firstReadDone.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            transactionTemplate.execute(status -> {
                Department d = departmentRepo.findById(deptId).orElseThrow();
                d.setBudget(new BigDecimal("17500000"));
                departmentRepo.saveAndFlush(d);
                log.info("[NON-REPEATABLE Writer] Committed budget=17500000");
                return null;
            });
            updateCommitted.countDown();
        });

        try {
            writerFuture.get(10, TimeUnit.SECONDS);
            List<BigDecimal> readings = readerFuture.get(10, TimeUnit.SECONDS);
            result.put("2_firstRead", readings.get(0).toString());
            result.put("3_secondRead", readings.get(1).toString());
            boolean nonRepeatable = readings.get(0).compareTo(readings.get(1)) != 0;
            result.put("4_nonRepeatableReadOccurred", nonRepeatable ? "YES" : "NO");
            result.put("5_lesson", "Under READ_COMMITTED, the same query can return different values " +
                    "within the same transaction if another transaction commits in between. " +
                    "Use REPEATABLE_READ to get a consistent snapshot.");
        } catch (Exception ex) {
            result.put("error", ex.getMessage());
        } finally {
            executor.shutdown();
            // Restore original budget
            transactionTemplate.execute(status -> {
                Department d = departmentRepo.findById(deptId).orElseThrow();
                d.setBudget(originalBudget);
                departmentRepo.save(d);
                return null;
            });
        }

        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. REPEATABLE READ DEMO — prevents non-repeatable reads
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * REPEATABLE READ — snapshot isolation
     * ────────────────────────────────────────────────────────────────────
     * WHAT HAPPENS:
     * Tx-A (REPEATABLE_READ): Reads department budget → 100000 (snapshot)
     * Tx-B: Updates budget to 300000 and COMMITS.
     * Tx-A: Reads the SAME department again → 100000 (SAME! snapshot)
     * <p>
     * REPEATABLE_READ takes a snapshot at the start of the transaction.
     * All reads within the transaction see data as it was at that moment.
     * Even if other transactions commit changes, Tx-A's reads are stable.
     * <p>
     * POSTGRESQL BONUS:
     * PostgreSQL's REPEATABLE_READ also prevents phantom reads (unlike
     * the SQL standard which says phantoms may still occur at this level).
     * This is because PostgreSQL uses MVCC-based Snapshot Isolation.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public Map<String, String> demoRepeatableRead(int deptId) {
        Map<String, String> result = new LinkedHashMap<>();
        Department dept = departmentRepo.findById(deptId).orElse(null);
        if (dept == null) {
            result.put("error", "Department " + deptId + " not found.");
            return result;
        }

        BigDecimal firstRead = dept.getBudget();
        result.put("1_firstRead", String.valueOf(firstRead));

        // Simulate another transaction modifying data between reads
        // (In a real scenario this would be a concurrent user)
        CountDownLatch committed = new CountDownLatch(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            transactionTemplate.execute(status -> {
                Department d = departmentRepo.findById(deptId).orElseThrow();
                d.setBudget(new BigDecimal("700000"));
                departmentRepo.saveAndFlush(d);
                log.info("[REPEATABLE-READ Writer] Committed budget=700000");
                return null;
            });
            committed.countDown();
        });

        try {
            committed.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        // Second read — should still see the original snapshot value
        entityManager.clear();
        Department dept2 = departmentRepo.findById(deptId).orElse(null);
        BigDecimal secondRead = dept2 != null ? dept2.getBudget() : BigDecimal.ZERO;
        result.put("2_secondRead", String.valueOf(secondRead));

        boolean repeatable = firstRead.compareTo(secondRead) == 0;
        result.put("3_readIsRepeatable", repeatable ? "YES — snapshot is consistent" : "NO");
        result.put("4_lesson", "REPEATABLE_READ uses a snapshot from the start of the transaction. " +
                "Even though another transaction committed a change, our reads return the same value.");

        // Cleanup happens outside this read-only transaction
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 4. PHANTOM READ & SERIALIZABLE DEMO
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * PHANTOM READ prevention with SERIALIZABLE
     * ────────────────────────────────────────────────────────────────────
     * WHAT IS A PHANTOM?
     * Transaction A: SELECT count(*) FROM employee WHERE dept_id = 1 → 5
     * Transaction B: INSERT INTO employee (dept_id = 1) and COMMITS.
     * Transaction A: Same SELECT → 6. The new row is a "phantom".
     * <p>
     * SERIALIZABLE prevents phantoms by making transactions appear as if
     * they executed one after another (serial order). In PostgreSQL, this
     * is implemented using Serializable Snapshot Isolation (SSI).
     * <p>
     * If a conflict is detected, PostgreSQL throws a serialization failure
     * and the application must RETRY the transaction.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
    public Map<String, String> demoSerializable(int deptId) {
        Map<String, String> result = new LinkedHashMap<>();

        // First range query — count employees in the department
        long firstCount = employeeRepo.countByDepartment_DeptId(deptId);
        result.put("1_firstCount", String.valueOf(firstCount));

        // Another transaction inserts a new employee in the same department
        CountDownLatch inserted = new CountDownLatch(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                transactionTemplate.execute(status -> {
                    Department dept = departmentRepo.findById(deptId).orElseThrow();
                    Employee phantom = Employee.builder()
                            .empName("Phantom Employee")
                            .email("phantom_" + System.currentTimeMillis() + "@test.com")
                            .salary(new BigDecimal("1000"))
                            .department(dept)
                            .location("Phantom City")
                            .build();
                    employeeRepo.saveAndFlush(phantom);
                    log.info("[SERIALIZABLE Writer] Inserted phantom employee");
                    return null;
                });
            } finally {
                inserted.countDown();
            }
        });

        try {
            inserted.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        // Second range query — same query within SERIALIZABLE transaction
        long secondCount = employeeRepo.countByDepartment_DeptId(deptId);
        result.put("2_secondCount", String.valueOf(secondCount));

        boolean phantomPrevented = (firstCount == secondCount);
        result.put("3_phantomPrevented", phantomPrevented
                ? "YES — SERIALIZABLE sees a stable snapshot, no phantoms"
                : "NO — phantom occurred");
        result.put("4_lesson", "SERIALIZABLE isolation prevents phantom reads. " +
                "PostgreSQL uses SSI (Serializable Snapshot Isolation) which may throw " +
                "serialization_failure errors requiring retry.");

        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 5. LOST UPDATE DEMO — Optimistic Locking with @Version
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * LOST UPDATE — two concurrent read-modify-write cycles
     * ────────────────────────────────────────────────────────────────────
     * THE PROBLEM:
     * Tx-A reads Employee (salary=5000, version=0)
     * Tx-B reads Employee (salary=5000, version=0)
     * Tx-A: salary = 5000 + 1000 = 6000, version=0 → 1 ✓ committed
     * Tx-B: salary = 5000 + 2000 = 7000, version=0 → ✗ FAILS!
     * Hibernate detects version mismatch → OptimisticLockException
     * <p>
     * Without @Version, Tx-B would silently overwrite Tx-A's change,
     * resulting in salary=7000 instead of the correct 8000.
     * <p>
     * THE SOLUTION:
     *
     * @Version on the entity automatically enables optimistic locking.
     * Hibernate adds "WHERE version = ?" to the UPDATE SQL.
     * If the version doesn't match, it throws OptimisticLockException.
     * <p>
     * ALTERNATIVE: PESSIMISTIC LOCKING
     * Use entityManager.lock(entity, LockModeType.PESSIMISTIC_WRITE)
     * or @Lock(LockModeType.PESSIMISTIC_WRITE) on a repository method.
     * This issues SELECT ... FOR UPDATE, blocking other transactions.
     * Prefer optimistic locking unless contention is very high.
     * ────────────────────────────────────────────────────────────────────
     */
    public Map<String, String> demoLostUpdate(int empId) {
        Map<String, String> result = new LinkedHashMap<>();
        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee " + empId + " not found. Create one first.");
            return result;
        }

        BigDecimal originalSalary = emp.getSalary();
        result.put("1_originalSalary", originalSalary.toString());
        result.put("2_originalVersion", String.valueOf(emp.getVersion()));

        CountDownLatch bothRead = new CountDownLatch(2);
        CountDownLatch txACommitted = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Tx-A: Adds 1000 to salary
        Future<String> txA = executor.submit(() -> {
            return transactionTemplate.execute(status -> {
                Employee e = employeeRepo.findById(empId).orElseThrow();
                log.info("[LOST-UPDATE Tx-A] Read salary={}, version={}", e.getSalary(), e.getVersion());
                bothRead.countDown();
                try {
                    bothRead.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }

                e.setSalary(e.getSalary().add(new BigDecimal("1000")));
                employeeRepo.saveAndFlush(e);
                log.info("[LOST-UPDATE Tx-A] Committed salary={}, version={}", e.getSalary(), e.getVersion());
                txACommitted.countDown();
                return "Tx-A committed salary=" + e.getSalary();
            });
        });

        // Tx-B: Adds 2000 to salary — should FAIL with OptimisticLockException
        Future<String> txB = executor.submit(() -> {
            try {
                return transactionTemplate.execute(status -> {
                    Employee e = employeeRepo.findById(empId).orElseThrow();
                    log.info("[LOST-UPDATE Tx-B] Read salary={}, version={}", e.getSalary(), e.getVersion());
                    bothRead.countDown();
                    // Wait for Tx-A to commit first
                    try {
                        txACommitted.await(5, TimeUnit.SECONDS);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }

                    e.setSalary(e.getSalary().add(new BigDecimal("2000")));
                    employeeRepo.saveAndFlush(e);
                    log.info("[LOST-UPDATE Tx-B] Committed salary={}", e.getSalary());
                    return "Tx-B committed salary=" + e.getSalary();
                });
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.info("[LOST-UPDATE Tx-B] OptimisticLockException — version mismatch!");
                return "Tx-B FAILED: " + ex.getClass().getSimpleName() + " — lost update PREVENTED by @Version!";
            }
        });

        try {
            result.put("3_txA_result", txA.get(10, TimeUnit.SECONDS));
            result.put("4_txB_result", txB.get(10, TimeUnit.SECONDS));
        } catch (Exception ex) {
            result.put("error", ex.getMessage());
        } finally {
            executor.shutdown();
        }

        // Final state
        Employee finalEmp = employeeRepo.findById(empId).orElse(null);
        if (finalEmp != null) {
            result.put("5_finalSalary", finalEmp.getSalary().toString());
            result.put("6_finalVersion", String.valueOf(finalEmp.getVersion()));
        }
        result.put("7_lesson", "Without @Version, Tx-B would overwrite Tx-A's raise (lost update). " +
                "With @Version, Hibernate detects the version mismatch and throws OptimisticLockException, " +
                "preventing data loss. The application should catch this and retry.");

        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 6. PESSIMISTIC LOCKING DEMO — SELECT FOR UPDATE
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * PESSIMISTIC LOCKING — prevents lost updates using DB-level locks
     * ────────────────────────────────────────────────────────────────────
     * Instead of detecting conflicts after the fact (optimistic locking),
     * pessimistic locking PREVENTS concurrent access by acquiring a
     * row-level lock (SELECT ... FOR UPDATE).
     * <p>
     * Tx-A: SELECT * FROM employee WHERE emp_id=1 FOR UPDATE (locks row)
     * Tx-B: Tries SELECT FOR UPDATE on same row → BLOCKS until Tx-A commits
     * Tx-A: Updates and commits → lock released
     * Tx-B: Unblocked, reads the LATEST committed value, applies its change
     * <p>
     * Result: Both raises are correctly applied (no lost update).
     * <p>
     * TRADE-OFF:
     * + Guaranteed correctness, no retry needed
     * - Reduced throughput due to blocking
     * - Risk of deadlocks if lock ordering is inconsistent
     * <p>
     * WHEN TO USE:
     * Use pessimistic locking when contention is HIGH (many concurrent
     * updates to the same row). Use optimistic locking (@Version) when
     * contention is LOW (conflicts are rare, retries are acceptable).
     * ────────────────────────────────────────────────────────────────────
     */
    public Map<String, String> demoPessimisticLocking(int empId) {
        Map<String, String> result = new LinkedHashMap<>();
        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee " + empId + " not found.");
            return result;
        }

        BigDecimal originalSalary = emp.getSalary();
        result.put("1_originalSalary", originalSalary.toString());

        CountDownLatch txALocked = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Tx-A: Locks row, adds 1000
        Future<String> txA = executor.submit(() ->
                transactionTemplate.execute(status -> {
                    Employee e = entityManager.find(Employee.class, empId, LockModeType.PESSIMISTIC_WRITE);
                    log.info("[PESSIMISTIC Tx-A] Locked row, salary={}", e.getSalary());
                    txALocked.countDown();
                    // Hold lock for a moment
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                    e.setSalary(e.getSalary().add(new BigDecimal("1000")));
                    entityManager.flush();
                    log.info("[PESSIMISTIC Tx-A] Committed salary={}", e.getSalary());
                    return "Tx-A committed salary=" + e.getSalary();
                })
        );

        // Tx-B: Also locks row, adds 2000 — will BLOCK until Tx-A releases
        Future<String> txB = executor.submit(() -> {
            try {
                txALocked.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return transactionTemplate.execute(status -> {
                log.info("[PESSIMISTIC Tx-B] Attempting to lock row...");
                Employee e = entityManager.find(Employee.class, empId, LockModeType.PESSIMISTIC_WRITE);
                log.info("[PESSIMISTIC Tx-B] Acquired lock, salary={}", e.getSalary());
                e.setSalary(e.getSalary().add(new BigDecimal("2000")));
                entityManager.flush();
                log.info("[PESSIMISTIC Tx-B] Committed salary={}", e.getSalary());
                return "Tx-B committed salary=" + e.getSalary();
            });
        });

        try {
            result.put("2_txA_result", txA.get(15, TimeUnit.SECONDS));
            result.put("3_txB_result", txB.get(15, TimeUnit.SECONDS));
        } catch (Exception ex) {
            result.put("error", ex.getMessage());
        } finally {
            executor.shutdown();
        }

        Employee finalEmp = employeeRepo.findById(empId).orElse(null);
        if (finalEmp != null) {
            result.put("4_finalSalary", finalEmp.getSalary().toString());
            BigDecimal expectedSalary = originalSalary.add(new BigDecimal("3000"));
            result.put("5_correctTotal", "Both raises applied: " + originalSalary + " + 1000 + 2000 = " + expectedSalary);
        }
        result.put("6_lesson", "Pessimistic locking (SELECT FOR UPDATE) blocks concurrent access, " +
                "ensuring both updates are applied sequentially. No lost update, no retry needed, " +
                "but reduced throughput due to blocking.");

        return result;
    }
}

