# Spring @Transactional — Comprehensive Learning Project

A hands-on revision guide covering **every aspect** of Spring's `@Transactional`
annotation: isolation levels, propagation types, concurrency problems (dirty reads,
lost updates, phantom reads), optimistic & pessimistic locking, and the difference
between Spring and Jakarta `@Transactional`.

> **Tech Stack:** Java 21 · Spring Boot 3.5 · PostgreSQL · JPA/Hibernate · Lombok · MapStruct

---

## Table of Contents

1. [Quick Start](#quick-start)
2. [Spring vs Jakarta @Transactional](#spring-vs-jakarta-transactional)
3. [How @Transactional Works (Proxy)](#how-transactional-works-proxy)
4. [Isolation Levels](#isolation-levels)
5. [Concurrency Problems & Solutions](#concurrency-problems--solutions)
6. [Propagation Types](#propagation-types)
7. [Other @Transactional Attributes](#other-transactional-attributes)
8. [Common Pitfalls](#common-pitfalls)
9. [API Endpoint Reference](#api-endpoint-reference)
10. [Project Structure](#project-structure)

---

## Quick Start

```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Run the application
./mvnw spring-boot:run

# 3. Create seed data
# Create a department
curl -X POST http://localhost:8080/departments/department \
  -H "Content-Type: application/json" \
  -d '{"deptName":"Engineering","budget":100000}'

# Create an employee (use the deptId returned above)
curl -X POST http://localhost:8080/employees/employee \
  -H "Content-Type: application/json" \
  -d '{"empName":"Alice","email":"alice@test.com","salary":5000,"deptId":1}'

# 4. Try the demos (see API Reference below)
curl http://localhost:8080/api/transaction-demos/isolation/lost-update?empId=1
curl http://localhost:8080/api/transaction-demos/propagation/required?empId=1&innerFails=false
```

---

## Spring vs Jakarta @Transactional

| Feature                    | Spring `@Transactional`                         | Jakarta `@Transactional`               |
|----------------------------|--------------------------------------------------|-----------------------------------------|
| **Package**                | `org.springframework.transaction.annotation`     | `jakarta.transaction`                   |
| **Propagation types**      | 7: REQUIRED, REQUIRES_NEW, SUPPORTS,             | 5: REQUIRED, REQUIRES_NEW, SUPPORTS,   |
|                            | NOT_SUPPORTED, MANDATORY, NEVER, **NESTED**      | NOT_SUPPORTED, MANDATORY               |
| **Isolation level**        | ✅ `isolation` attribute (4 levels + DEFAULT)     | ❌ Not supported                        |
| **Read-only flag**         | ✅ `readOnly` attribute                           | ❌ Not supported                        |
| **Timeout**                | ✅ `timeout` / `timeoutString`                    | ❌ Not supported                        |
| **Rollback control**       | `rollbackFor`, `noRollbackFor`                   | `rollbackOn`, `dontRollbackOn`          |
| **Transaction manager**    | ✅ `transactionManager` / `value`                 | ❌ Not supported                        |
| **Labels (observability)** | ✅ `label` (Spring 5.3+)                          | ❌ Not supported                        |
| **Works in Spring?**       | ✅ Native                                         | ✅ Spring recognises it via             |
|                            |                                                  | `JtaTransactionAnnotationParser`        |
| **Default rollback**       | Unchecked exceptions only (RuntimeException/Error)| Unchecked exceptions only               |

> **Recommendation:** Always use **Spring's** `@Transactional` in Spring applications.
> Jakarta's works but gives you fewer features. See `EmployeeServiceImpl.java` for
> a detailed comparison in code comments.

---

## How @Transactional Works (Proxy)

Spring implements `@Transactional` using **AOP proxies** (either JDK dynamic proxies
or CGLIB subclasses):

```
Controller → Proxy → Actual Bean Method
             ↓
         begin Tx
             ↓
         call real method
             ↓
         commit / rollback
```

The proxy **intercepts** the method call, begins a transaction, invokes the real method,
and commits or rolls back based on the outcome.

### Why Self-Invocation Fails

```java
@Service
public class MyService {
    @Transactional
    public void methodA() {
        this.methodB(); // ← BYPASSES the proxy! @Transactional is IGNORED
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void methodB() { /* ... */ }
}
```

When `methodA()` calls `this.methodB()`, the call goes directly to the object — not
through the proxy. So `methodB()`'s `@Transactional` annotation is **completely ignored**.

**Fix:** Inject the bean into itself, or (better) move `methodB()` to a **separate bean**.
This project uses `PropagationOuterService` → `PropagationInnerService` to demonstrate
correct cross-bean calls.

---

## Isolation Levels

Isolation levels control what data a transaction can "see" from other concurrent
transactions. Higher isolation = more consistency but less concurrency.

| Level              | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
|--------------------|:----------:|:-------------------:|:------------:|:-----------:|
| READ_UNCOMMITTED   | ⚠️ Possible | ⚠️ Possible          | ⚠️ Possible   | Fastest     |
| READ_COMMITTED     | ✅ Prevented| ⚠️ Possible          | ⚠️ Possible   | Fast        |
| REPEATABLE_READ    | ✅ Prevented| ✅ Prevented          | ⚠️ Possible*  | Moderate    |
| SERIALIZABLE       | ✅ Prevented| ✅ Prevented          | ✅ Prevented   | Slowest     |

> \* PostgreSQL's REPEATABLE_READ also prevents phantom reads (MVCC Snapshot Isolation).

### Usage in Code

```java
@Transactional(isolation = Isolation.REPEATABLE_READ)
public void myMethod() { /* ... */ }
```

### PostgreSQL Caveats

- **READ_UNCOMMITTED** is silently upgraded to **READ_COMMITTED** (PostgreSQL never allows dirty reads).
- **REPEATABLE_READ** uses MVCC snapshots, so it also prevents phantom reads (stronger than SQL standard).
- **SERIALIZABLE** uses Serializable Snapshot Isolation (SSI) — may throw `SerializationFailure` errors requiring retry.

### Default Isolation

```java
@Transactional
// isolation = Isolation.DEFAULT → uses the DB's default
```
PostgreSQL's default is **READ_COMMITTED**.

---

## Concurrency Problems & Solutions

### 1. Dirty Read

| | Description |
|--|--|
| **Problem** | Tx-B reads data that Tx-A has modified but **not yet committed**. If Tx-A rolls back, Tx-B has used "phantom" data. |
| **Example** | Tx-A updates salary 5000→9000 (uncommitted). Tx-B reads 9000. Tx-A rolls back. Tx-B used wrong data. |
| **Solution** | Use `READ_COMMITTED` or higher (PostgreSQL does this automatically). |
| **Demo endpoint** | `GET /api/transaction-demos/isolation/dirty-read?empId=1` |

### 2. Non-Repeatable Read

| | Description |
|--|--|
| **Problem** | Tx-A reads a row, Tx-B modifies and **commits**, Tx-A reads again → **different value**. |
| **Example** | Tx-A reads budget=100000. Tx-B updates to 200000 and commits. Tx-A reads again → 200000. |
| **Solution** | Use `REPEATABLE_READ` — snapshot taken at Tx start, all reads see consistent data. |
| **Demo endpoint** | `GET /api/transaction-demos/isolation/non-repeatable-read?deptId=1` |

### 3. Phantom Read

| | Description |
|--|--|
| **Problem** | Tx-A runs a range query, Tx-B **inserts/deletes** matching rows and commits, Tx-A re-runs → different row count. |
| **Example** | Tx-A counts 5 employees in dept 1. Tx-B inserts one and commits. Tx-A counts again → 6. |
| **Solution** | Use `SERIALIZABLE` — transactions appear to execute in serial order. |
| **Demo endpoint** | `GET /api/transaction-demos/isolation/serializable?deptId=1` |

### 4. Lost Update

| | Description |
|--|--|
| **Problem** | Two transactions read the same row, both modify it, second commit **overwrites** the first. |
| **Example** | Both read salary=5000. Tx-A sets 6000 (+1000). Tx-B sets 7000 (+2000). Tx-A's raise is **lost**. Correct answer should be 8000. |
| **Solution (Optimistic)** | `@Version` on the entity. Hibernate adds `WHERE version=?` to UPDATE. Second transaction gets `OptimisticLockException`. |
| **Solution (Pessimistic)** | `SELECT ... FOR UPDATE` (`LockModeType.PESSIMISTIC_WRITE`). Blocks concurrent reads until lock is released. |
| **Demo endpoints** | `GET /api/transaction-demos/isolation/lost-update?empId=1` |
| | `GET /api/transaction-demos/isolation/pessimistic-locking?empId=1` |

### Optimistic vs Pessimistic Locking

| | Optimistic (@Version) | Pessimistic (SELECT FOR UPDATE) |
|--|--|--|
| **Mechanism** | Version check at UPDATE time | Row lock at SELECT time |
| **Blocking** | No blocking (fail-fast) | Blocks concurrent transactions |
| **Performance** | Better under low contention | Better under high contention |
| **Retry needed?** | Yes (on OptimisticLockException) | No |
| **Deadlock risk** | No | Yes (if lock ordering is inconsistent) |

---

## Propagation Types

Propagation controls how a transaction relates to any **existing** transaction from
the calling method. There are **7 types** in Spring:

| # | Propagation     | Existing Tx? | Behaviour                                      |
|---|-----------------|:------------:|-------------------------------------------------|
| 1 | **REQUIRED**    | Yes          | Join it                                         |
|   | *(default)*     | No           | Create new                                      |
| 2 | **REQUIRES_NEW**| Yes          | **Suspend** existing, create new                |
|   |                 | No           | Create new                                      |
| 3 | **SUPPORTS**    | Yes          | Join it                                         |
|   |                 | No           | Run **without** transaction                     |
| 4 | **NOT_SUPPORTED**| Yes         | **Suspend** existing, run without Tx            |
|   |                 | No           | Run without Tx                                  |
| 5 | **MANDATORY**   | Yes          | Join it                                         |
|   |                 | No           | ❌ Throw `IllegalTransactionStateException`      |
| 6 | **NEVER**       | Yes          | ❌ Throw `IllegalTransactionStateException`      |
|   |                 | No           | Run without Tx                                  |
| 7 | **NESTED**      | Yes          | Create **savepoint** (partial rollback possible)|
|   |                 | No           | Create new (like REQUIRED)                      |

### Usage in Code

```java
// Inner service bean (called by outer service)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void auditLog(String action) { /* ... */ }
```

### Key Scenarios

#### REQUIRED — Shared Fate
```
Outer Tx ──────────────────────────────────────────────────
    │ update employee │ call inner(REQUIRED) │ commit/rollback
    │                 └───── same Tx ────────┘
```
If inner fails → entire Tx rolls back (both changes lost).

#### REQUIRES_NEW — Independent Commit
```
Outer Tx ──────── SUSPENDED ──────── RESUMED ──── rollback
                      │                  │
Inner Tx              └── commit ────────┘
```
Inner commits independently. Outer can roll back without affecting inner.

#### NESTED — Savepoint
```
Outer Tx ───────────────────────────────────────── commit
    │ update employee │ SAVEPOINT │ inner fails │ rollback to savepoint │
    │                 │           └─────────────┘                       │
    │                 │ outer continues ────────────────────────────────┘
```
Inner failure rolls back to savepoint only. Outer can catch and continue.

> **Note:** NESTED requires savepoint support. JPA/Hibernate may not support it.
> Works with `DataSourceTransactionManager` + `JdbcTemplate`.

---

## Other @Transactional Attributes

| Attribute           | Description                                                            | Example                                  |
|---------------------|------------------------------------------------------------------------|------------------------------------------|
| `readOnly`          | Hint for Hibernate to skip dirty-checking & suppress flush             | `@Transactional(readOnly = true)`        |
| `timeout`           | Max seconds before auto-rollback                                       | `@Transactional(timeout = 30)`           |
| `rollbackFor`       | Rollback on these exceptions (including checked)                       | `@Transactional(rollbackFor = Exception.class)` |
| `noRollbackFor`     | Do NOT rollback on these exceptions                                    | `@Transactional(noRollbackFor = BusinessException.class)` |
| `transactionManager`| Specify which TxManager bean to use                                    | `@Transactional("jpaTransactionManager")`|
| `label`             | Descriptive labels for observability (Spring 5.3+)                     | `@Transactional(label = "createOrder")` |

### `readOnly = true` Deep Dive

```java
@Transactional(readOnly = true)
public DepartmentDTO fetchDepartment(int id) { /* ... */ }
```

What Spring/Hibernate does:
1. Calls `connection.setReadOnly(true)` → JDBC driver may route to read-replica
2. Hibernate sets flush mode to `MANUAL` → no automatic flush
3. Hibernate skips dirty-checking snapshot → less memory, faster GC

---

## Common Pitfalls

### 1. Self-Invocation (Proxy Bypass)
```java
public void methodA() {
    this.methodB(); // ❌ @Transactional on methodB is IGNORED!
}
```
**Fix:** Move `methodB()` to a separate `@Service` bean.

### 2. Checked Exceptions Don't Rollback
```java
@Transactional
public void save() throws IOException {
    // If IOException is thrown, transaction COMMITS (not rolls back)!
}
```
**Fix:** Add `rollbackFor = Exception.class` or `rollbackFor = IOException.class`.

### 3. @Transactional on Private Methods
```java
@Transactional  // ❌ IGNORED — proxy cannot intercept private methods
private void doWork() { /* ... */ }
```
**Fix:** Make the method `public` (or at least package-private for CGLIB proxies).

### 4. Wrong @Transactional Import
```java
import jakarta.transaction.Transactional; // ❌ Loses isolation, readOnly, timeout
import org.springframework.transaction.annotation.Transactional; // ✅ Full features
```

### 5. Catching Exceptions That Suppress Rollback
```java
@Transactional
public void outer() {
    try {
        innerService.methodWithRequiredPropagation(); // throws RuntimeException
    } catch (RuntimeException ex) {
        // You caught it, but the transaction is ALREADY marked for rollback!
        // Spring will throw UnexpectedRollbackException at commit time.
    }
}
```
**Fix:** Use `REQUIRES_NEW` for the inner method if you want to catch its failure
without affecting the outer transaction.

### 6. Long-Running Transactions
Holding a transaction open for too long (e.g., making HTTP calls within a Tx)
ties up a database connection and can cause pool exhaustion.
**Fix:** Use `NOT_SUPPORTED` propagation for non-DB operations, or restructure
to minimize transaction scope.

---

## API Endpoint Reference

### Setup Endpoints (create test data first)

| Method | URL                              | Body (JSON)                                                                                |
|--------|----------------------------------|--------------------------------------------------------------------------------------------|
| POST   | `/departments/department`        | `{"deptName":"Engineering","budget":100000}`                                               |
| POST   | `/employees/employee`            | `{"empName":"Alice","email":"alice@test.com","salary":5000,"deptId":1}`                    |

### Isolation Level Demos

| Endpoint                                            | What It Demonstrates                                    |
|-----------------------------------------------------|---------------------------------------------------------|
| `GET /api/transaction-demos/isolation/dirty-read?empId=1`           | Dirty read attempt (PostgreSQL prevents it)  |
| `GET /api/transaction-demos/isolation/non-repeatable-read?deptId=1` | Non-repeatable read under READ_COMMITTED     |
| `GET /api/transaction-demos/isolation/repeatable-read?deptId=1`     | Snapshot consistency with REPEATABLE_READ    |
| `GET /api/transaction-demos/isolation/serializable?deptId=1`        | Phantom read prevention with SERIALIZABLE    |
| `GET /api/transaction-demos/isolation/lost-update?empId=1`          | Lost update prevention with @Version         |
| `GET /api/transaction-demos/isolation/pessimistic-locking?empId=1`  | SELECT FOR UPDATE blocking                   |

### Propagation Demos

| Endpoint                                                                 | What It Demonstrates                         |
|--------------------------------------------------------------------------|----------------------------------------------|
| `GET /api/transaction-demos/propagation/required?empId=1&innerFails=false`  | REQUIRED — shared transaction             |
| `GET /api/transaction-demos/propagation/required?empId=1&innerFails=true`   | REQUIRED — shared rollback                |
| `GET /api/transaction-demos/propagation/requires-new?empId=1&outerFails=false` | REQUIRES_NEW — independent commit       |
| `GET /api/transaction-demos/propagation/requires-new?empId=1&outerFails=true`  | REQUIRES_NEW — inner survives rollback  |
| `GET /api/transaction-demos/propagation/supports-with-tx`                   | SUPPORTS — joins existing Tx              |
| `GET /api/transaction-demos/propagation/supports-without-tx`                | SUPPORTS — runs without Tx                |
| `GET /api/transaction-demos/propagation/not-supported?empId=1`              | NOT_SUPPORTED — suspends Tx               |
| `GET /api/transaction-demos/propagation/mandatory-with-tx`                  | MANDATORY — succeeds with Tx              |
| `GET /api/transaction-demos/propagation/mandatory-without-tx`               | MANDATORY — fails without Tx              |
| `GET /api/transaction-demos/propagation/never-with-tx`                      | NEVER — fails with Tx                     |
| `GET /api/transaction-demos/propagation/never-without-tx`                   | NEVER — succeeds without Tx               |
| `GET /api/transaction-demos/propagation/nested?empId=1`                     | NESTED — savepoint / partial rollback     |

---

## Project Structure

```
src/main/java/com/epam/employee/
├── EmployeeDeptApplication.java         # Spring Boot entry point
├── controller/
│   ├── DepartmentController.java        # CRUD for departments
│   ├── EmployeeController.java          # CRUD for employees
│   └── TransactionDemoController.java   # ★ Demo endpoints for isolation & propagation
├── dto/
│   ├── DepartmentDTO.java
│   ├── EmployeeDTO.java                 # Includes 'version' for optimistic locking
│   └── ...
├── entity/
│   ├── Department.java                  # DEPARTMENT table
│   ├── Employee.java                    # EMPLOYEE table (has @Version)
│   ├── AuditLog.java                    # ★ AUDIT_LOG table (for propagation demos)
│   └── ...
├── mapper/
│   ├── DepartmentMapper.java            # MapStruct DTO ↔ Entity
│   └── EmployeeMapper.java
├── repo/
│   ├── DepartmentRepo.java
│   ├── EmployeeRepo.java
│   └── AuditLogRepo.java               # ★
└── service/
    ├── DepartmentService.java           # Interface
    ├── EmployeeService.java             # Interface
    └── impl/
        ├── DepartmentServiceImpl.java   # ★ Basic @Transactional (documented)
        ├── EmployeeServiceImpl.java     # ★ Spring vs Jakarta comparison (documented)
        ├── IsolationDemoService.java    # ★ All isolation levels + locking demos
        ├── PropagationInnerService.java # ★ Inner bean — 7 propagation methods
        └── PropagationOuterService.java # ★ Outer bean — orchestrates propagation demos
```

Files marked with ★ are the key learning files — read their Javadoc comments carefully.

---

## Quick Revision Cheatsheet

```
ISOLATION LEVELS (what can other Tx see?)
─────────────────────────────────────────
READ_UNCOMMITTED → sees uncommitted data (dirty reads)
READ_COMMITTED   → sees only committed data (default in PostgreSQL)
REPEATABLE_READ  → snapshot at Tx start (no changing reads)
SERIALIZABLE     → full isolation (no phantoms, serial execution)

PROPAGATION (how does inner Tx relate to outer?)
─────────────────────────────────────────────────
REQUIRED      → join or create          (default, shared fate)
REQUIRES_NEW  → always new, suspend     (independent, audit logs)
SUPPORTS      → join or none            (optional Tx)
NOT_SUPPORTED → suspend, run without    (external calls)
MANDATORY     → must exist, else error  (enforce contract)
NEVER         → must NOT exist, error   (prevent accidental Tx)
NESTED        → savepoint               (partial rollback)

ROLLBACK RULES
──────────────
RuntimeException / Error    → rollback (default)
Checked exception           → commit (default!) — use rollbackFor to change

LOCKING
───────
@Version (optimistic)       → fails fast, retry needed, no blocking
PESSIMISTIC_WRITE           → blocks, no retry needed, deadlock risk
```
