package com.epam.employee.service.impl;

import com.epam.employee.entity.Employee;
import com.epam.employee.repo.AuditLogRepo;
import com.epam.employee.repo.DepartmentRepo;
import com.epam.employee.repo.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * PropagationOuterService — OUTER (calling) bean for propagation demos
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * This service demonstrates all 7 Spring propagation types by calling
 * methods on {@link PropagationInnerService} (a SEPARATE bean, so the
 * AOP proxy intercepts the call and applies the correct propagation).
 *
 * ┌────────────────────────────────────────────────────────────────────────┐
 * │  SELF-INVOCATION PITFALL                                              │
 * │                                                                        │
 * │  If this class called its OWN @Transactional method internally,       │
 * │  the call would bypass the proxy and the annotation would be IGNORED. │
 * │                                                                        │
 * │  Example of what NOT to do:                                            │
 * │    public void outerMethod() {                                         │
 * │        this.innerMethod();  // ← BYPASSES proxy, no Tx management!   │
 * │    }                                                                   │
 * │                                                                        │
 * │  Correct approach: call a method on a DIFFERENT injected bean.        │
 * └────────────────────────────────────────────────────────────────────────┘
 *
 * SCENARIO FOR EACH DEMO:
 *   1. Outer method starts a transaction and creates/updates an Employee.
 *   2. It calls the inner service to log an audit entry (with a specific
 *      propagation type).
 *   3. Depending on the demo, the inner or outer method may throw to
 *      demonstrate rollback behaviour.
 *   4. Results are returned as a Map describing what happened.
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Service
public class PropagationOuterService {

    private static final Logger log = LoggerFactory.getLogger(PropagationOuterService.class);

    private final PropagationInnerService innerService;
    private final EmployeeRepo employeeRepo;
    private final AuditLogRepo auditLogRepo;

    @Autowired
    public PropagationOuterService(PropagationInnerService innerService,
                                   EmployeeRepo employeeRepo,
                                   AuditLogRepo auditLogRepo) {
        this.innerService = innerService;
        this.employeeRepo = employeeRepo;
        this.auditLogRepo = auditLogRepo;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 1. REQUIRED — both outer and inner share the SAME transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: REQUIRED propagation
     * ────────────────────────────────────────────────────────────────────
     * SCENARIO A (innerFails = false):
     *   Outer Tx → updates employee salary → inner logs audit (REQUIRED)
     *   → both commit together ✓
     *
     * SCENARIO B (innerFails = true):
     *   Outer Tx → updates employee salary → inner throws RuntimeException
     *   → ENTIRE transaction rolls back (salary update AND audit log)
     *   because REQUIRED means inner JOINS the outer transaction.
     *
     * KEY INSIGHT: With REQUIRED, there is only ONE physical transaction.
     * If ANY participant marks it for rollback, EVERYTHING rolls back.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoRequired(int empId, boolean innerFails) {
        Map<String, String> result = new LinkedHashMap<>();
        long auditCountBefore = auditLogRepo.count();

        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee not found");
            return result;
        }

        result.put("1_salaryBefore", emp.getSalary().toString());
        emp.setSalary(emp.getSalary().add(new BigDecimal("500")));
        employeeRepo.saveAndFlush(emp);
        result.put("2_salaryAfterUpdate", emp.getSalary().toString());

        try {
            innerService.innerRequired("REQUIRED_DEMO", innerFails);
            result.put("3_innerResult", "SUCCESS — audit log written in SAME transaction");
        } catch (RuntimeException ex) {
            result.put("3_innerResult", "FAILED — " + ex.getMessage());
            result.put("4_rollback", "Entire transaction marked for rollback (salary + audit)");
            throw ex; // re-throw to actually trigger rollback
        }

        result.put("4_auditLogCount", "Before=" + auditCountBefore + ", After=" + auditLogRepo.count());
        result.put("5_lesson", "REQUIRED: inner joins outer Tx. If inner fails, " +
                "outer is also rolled back because they share the same Tx.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. REQUIRES_NEW — inner has its OWN independent transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: REQUIRES_NEW propagation
     * ────────────────────────────────────────────────────────────────────
     * SCENARIO (outerFails = true):
     *   Outer Tx → updates employee salary → inner logs audit (REQUIRES_NEW,
     *   committed in its own Tx) → outer throws RuntimeException → outer
     *   rolls back, but audit log SURVIVES because it was in a separate Tx.
     *
     * KEY INSIGHT: REQUIRES_NEW suspends the outer transaction, opens a
     * new one for the inner method, commits it independently, then
     * resumes the outer transaction.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoRequiresNew(int empId, boolean outerFails) {
        Map<String, String> result = new LinkedHashMap<>();
        long auditCountBefore = auditLogRepo.count();

        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee not found");
            return result;
        }

        result.put("1_salaryBefore", emp.getSalary().toString());
        emp.setSalary(emp.getSalary().add(new BigDecimal("500")));
        employeeRepo.saveAndFlush(emp);
        result.put("2_salaryAfterUpdate", emp.getSalary().toString());

        // Inner runs in its OWN transaction
        innerService.innerRequiresNew("REQUIRES_NEW_DEMO", false);
        result.put("3_innerResult", "Audit committed in SEPARATE transaction");

        if (outerFails) {
            result.put("4_outerFails", "YES — outer rolls back, but audit log survives!");
            throw new RuntimeException("Outer transaction fails AFTER inner committed");
        }

        long auditCountAfter = auditLogRepo.count();
        result.put("4_auditLogCount", "Before=" + auditCountBefore + ", After=" + auditCountAfter);
        result.put("5_lesson", "REQUIRES_NEW: inner gets its own Tx. Even if outer rolls back, " +
                "inner's changes are already committed. Useful for audit logs.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. SUPPORTS — transactional only if caller is
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: SUPPORTS propagation — called WITH a transaction
     * ────────────────────────────────────────────────────────────────────
     * Since this outer method IS @Transactional, the inner method (SUPPORTS)
     * will participate in the outer transaction.
     *
     * If called from a non-transactional context (see the controller
     * endpoint), the inner method runs without a transaction.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoSupportsWithTx() {
        Map<String, String> result = new LinkedHashMap<>();
        innerService.innerSupports("SUPPORTS_WITH_TX", false);
        result.put("1_calledFrom", "Transactional outer method");
        result.put("2_innerBehavior", "JOINED outer transaction (because one exists)");
        result.put("3_lesson", "SUPPORTS: runs in Tx if one exists, otherwise runs without. " +
                "Good for read-only methods that benefit from a Tx but don't require one.");
        return result;
    }

    /**
     * Demo: SUPPORTS propagation — called WITHOUT a transaction
     * ────────────────────────────────────────────────────────────────────
     * This method is NOT @Transactional, so the inner (SUPPORTS) will
     * run without a transaction.
     * ────────────────────────────────────────────────────────────────────
     */
    // NO @Transactional — intentional!
    public Map<String, String> demoSupportsWithoutTx() {
        Map<String, String> result = new LinkedHashMap<>();
        innerService.innerSupports("SUPPORTS_WITHOUT_TX", false);
        result.put("1_calledFrom", "Non-transactional outer method");
        result.put("2_innerBehavior", "Ran WITHOUT transaction (because none existed)");
        result.put("3_lesson", "SUPPORTS: adapts to the calling context. No Tx from caller = no Tx for inner.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 4. NOT_SUPPORTED — always suspends any existing transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: NOT_SUPPORTED propagation
     * ────────────────────────────────────────────────────────────────────
     * Even though this outer method IS @Transactional, the inner method
     * (NOT_SUPPORTED) SUSPENDS the outer Tx and runs non-transactionally.
     *
     * CONSEQUENCE: The audit log written by the inner method auto-commits
     * immediately. If the outer transaction later rolls back, the audit
     * log remains.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoNotSupported(int empId) {
        Map<String, String> result = new LinkedHashMap<>();
        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee not found");
            return result;
        }

        emp.setSalary(emp.getSalary().add(new BigDecimal("100")));
        employeeRepo.saveAndFlush(emp);
        result.put("1_salaryUpdated", emp.getSalary().toString());

        // Inner SUSPENDS our transaction and runs without Tx
        innerService.innerNotSupported("NOT_SUPPORTED_DEMO", false);

        result.put("2_innerResult", "Audit log auto-committed (no Tx)");
        result.put("3_lesson", "NOT_SUPPORTED: suspends any existing Tx and runs without one. " +
                "Useful for long-running reads or external API calls that shouldn't hold a Tx open.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 5. MANDATORY — must have an existing transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: MANDATORY propagation — called WITH a transaction (succeeds)
     * ────────────────────────────────────────────────────────────────────
     * Since this outer method IS @Transactional, the inner (MANDATORY)
     * call succeeds — it joins the existing transaction.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(readOnly = true)
    public Map<String, String> demoMandatoryWithTx() {
        Map<String, String> result = new LinkedHashMap<>();
        innerService.innerMandatory("MANDATORY_WITH_TX", false);
        result.put("1_result", "SUCCESS — inner joined the existing transaction");
        result.put("2_lesson", "MANDATORY: requires an existing Tx. If one exists, joins it. " +
                "If none exists, throws IllegalTransactionStateException.");
        return result;
    }

    /**
     * Demo: MANDATORY propagation — called WITHOUT a transaction (FAILS)
     * ────────────────────────────────────────────────────────────────────
     * This method is NOT @Transactional. Calling innerMandatory() without
     * a transaction will throw IllegalTransactionStateException.
     * ────────────────────────────────────────────────────────────────────
     */
    //@Transactional //— intentional!
    public Map<String, String> demoMandatoryWithoutTx() {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            innerService.innerMandatory("MANDATORY_WITHOUT_TX", false);
            result.put("1_result", "Unexpectedly succeeded (should not happen)");
        } catch (Exception ex) {
            result.put("1_result", "EXCEPTION: " + ex.getClass().getSimpleName());
            result.put("2_message", ex.getMessage());
            result.put("3_lesson", "MANDATORY: throws IllegalTransactionStateException when called " +
                    "without an existing transaction. Use this to enforce that a method is never " +
                    "called standalone.");
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 6. NEVER — must NOT have a transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: NEVER propagation — called WITH a transaction (FAILS)
     * ────────────────────────────────────────────────────────────────────
     * This outer method IS @Transactional. Calling innerNever() within
     * a transaction will throw IllegalTransactionStateException.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoNeverWithTx() {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            innerService.innerNever("NEVER_WITH_TX", false);
            result.put("1_result", "Unexpectedly succeeded (should not happen)");
        } catch (Exception ex) {
            result.put("1_result", "EXCEPTION: " + ex.getClass().getSimpleName());
            result.put("2_message", ex.getMessage());
            result.put("3_lesson", "NEVER: throws IllegalTransactionStateException when called " +
                    "within an existing transaction. Opposite of MANDATORY.");
        }
        return result;
    }

    /**
     * Demo: NEVER propagation — called WITHOUT a transaction (succeeds)
     * ────────────────────────────────────────────────────────────────────
     * This method is NOT @Transactional. Calling innerNever() without
     * a transaction succeeds normally.
     * ────────────────────────────────────────────────────────────────────
     */
    // NO @Transactional — intentional!
    public Map<String, String> demoNeverWithoutTx() {
        Map<String, String> result = new LinkedHashMap<>();
        innerService.innerNever("NEVER_WITHOUT_TX", false);
        result.put("1_result", "SUCCESS — no transaction existed, so NEVER is satisfied");
        result.put("2_lesson", "NEVER: runs only without a transaction. " +
                "Use for operations that must not be tied to a DB transaction.");
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 7. NESTED — savepoint within existing transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Demo: NESTED propagation — partial rollback with savepoint
     * ────────────────────────────────────────────────────────────────────
     * SCENARIO:
     *   Outer Tx → updates employee salary → inner logs audit (NESTED).
     *   If inner fails → only the audit log is rolled back (to savepoint);
     *   the salary update can still commit.
     *
     * NOTE: Hibernate/JPA does not natively support NESTED propagation.
     *   With JpaTransactionManager, you may get:
     *     NestedTransactionNotSupportedException
     *   This works with DataSourceTransactionManager + JdbcTemplate, or
     *   if JpaTransactionManager.setNestedTransactionAllowed(true) is set
     *   and the underlying DataSource supports savepoints.
     *
     * DIFFERENCE FROM REQUIRES_NEW:
     *   REQUIRES_NEW → completely independent transaction (2 connections)
     *   NESTED → same physical transaction, but with a savepoint
     *            (1 connection, outer can see inner's changes before commit)
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional
    public Map<String, String> demoNested(int empId) {
        Map<String, String> result = new LinkedHashMap<>();
        Employee emp = employeeRepo.findById(empId).orElse(null);
        if (emp == null) {
            result.put("error", "Employee not found");
            return result;
        }

        emp.setSalary(emp.getSalary().add(new BigDecimal("100")));
        employeeRepo.saveAndFlush(emp);
        result.put("1_salaryUpdated", emp.getSalary().toString());

        try {
            // Inner uses NESTED — creates a savepoint
            innerService.innerNested("NESTED_DEMO", true); // intentionally fails
            result.put("2_innerResult", "Succeeded");
        } catch (RuntimeException ex) {
            // Inner rolled back to savepoint, but outer can continue
            result.put("2_innerResult", "FAILED and rolled back to savepoint: " + ex.getMessage());
            result.put("3_outerContinues", "YES — outer transaction is still active, salary update survives");
        }

        result.put("4_lesson", "NESTED: creates a savepoint. If inner fails, only inner changes " +
                "are rolled back. Outer can catch the exception and continue. " +
                "NOTE: May not work with JPA/Hibernate — see docs in PropagationInnerService.");
        return result;
    }
}

