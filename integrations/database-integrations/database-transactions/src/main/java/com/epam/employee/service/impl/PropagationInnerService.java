package com.epam.employee.service.impl;

import com.epam.employee.entity.AuditLog;
import com.epam.employee.repo.AuditLogRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * PropagationInnerService — INNER (called) bean for propagation demos
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * WHY A SEPARATE BEAN?
 * ─────────────────────
 * Spring's @Transactional is implemented using AOP proxies. When a bean calls
 * its OWN method internally, the call bypasses the proxy — so @Transactional
 * on the called method is IGNORED (the "self-invocation" pitfall).
 *
 * To demonstrate propagation correctly, the OUTER service must call methods
 * on a DIFFERENT bean (this one), so the proxy intercepts the call and
 * applies the correct propagation behaviour.
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │                     PROPAGATION TYPES REFERENCE                        │
 * ├─────────────────────┬──────────────────────────────────────────────────┤
 * │ Propagation         │ Behaviour                                        │
 * ├─────────────────────┼──────────────────────────────────────────────────┤
 * │ REQUIRED (default)  │ Join existing Tx, or create new if none exists  │
 * │ REQUIRES_NEW        │ Always create a NEW Tx; suspend existing if any │
 * │ SUPPORTS            │ Use existing Tx if present; else run without Tx │
 * │ NOT_SUPPORTED       │ Suspend existing Tx; always run without Tx      │
 * │ MANDATORY           │ MUST run within existing Tx; else → exception   │
 * │ NEVER               │ Must NOT run within a Tx; else → exception      │
 * │ NESTED              │ Create savepoint within existing Tx             │
 * └─────────────────────┴──────────────────────────────────────────────────┘
 *
 * Each method below persists an AuditLog entry and logs whether it is
 * running inside an active transaction. The OUTER service controls
 * whether a transaction exists when calling these methods.
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Service
public class PropagationInnerService {

    private static final Logger log = LoggerFactory.getLogger(PropagationInnerService.class);

    private final AuditLogRepo auditLogRepo;

    @Autowired
    public PropagationInnerService(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 1. REQUIRED — the default propagation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * REQUIRED (default):
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction ALREADY EXISTS → JOIN it (same Tx boundary).
     * • If NO transaction exists → CREATE a new one.
     *
     * CONSEQUENCE:
     *   If this method throws and the outer method catches the exception,
     *   the entire transaction is still marked for rollback (because both
     *   share the SAME physical transaction).
     *
     * USE WHEN: You want the method to participate in the caller's Tx.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void innerRequired(String action, boolean shouldFail) {
        logTransactionStatus("REQUIRED");
        saveAuditLog(action, "REQUIRED");
        if (shouldFail) {
            throw new RuntimeException("[REQUIRED] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. REQUIRES_NEW — always a fresh transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * REQUIRES_NEW:
     * ────────────────────────────────────────────────────────────────────
     * • ALWAYS creates a NEW physical transaction.
     * • If an outer transaction exists → it is SUSPENDED until this
     *   method completes (commit or rollback).
     *
     * CONSEQUENCE:
     *   The audit log written here is committed INDEPENDENTLY of the outer
     *   transaction. If the outer transaction rolls back, the audit log
     *   STILL EXISTS in the database.
     *
     * USE WHEN: You need guaranteed persistence regardless of the caller's
     *           outcome — e.g., audit logs, notification records.
     *
     * IMPORTANT: This creates a SECOND database connection while the
     *            outer connection is suspended. With connection pools,
     *            be careful of pool exhaustion under heavy load.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerRequiresNew(String action, boolean shouldFail) {
        logTransactionStatus("REQUIRES_NEW");
        saveAuditLog(action, "REQUIRES_NEW");
        if (shouldFail) {
            throw new RuntimeException("[REQUIRES_NEW] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. SUPPORTS — transactional only if caller is
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * SUPPORTS:
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction exists → JOIN it.
     * • If NO transaction exists → run WITHOUT a transaction.
     *
     * CONSEQUENCE:
     *   The method adapts to its calling context. If called from a
     *   @Transactional method, it participates. If called from a
     *   non-transactional method (e.g., controller), it runs without Tx.
     *
     * USE WHEN: Read-only operations that benefit from a transaction
     *           (e.g., Hibernate session) but don't require one.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void innerSupports(String action, boolean shouldFail) {
        logTransactionStatus("SUPPORTS");
        saveAuditLog(action, "SUPPORTS");
        if (shouldFail) {
            throw new RuntimeException("[SUPPORTS] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 4. NOT_SUPPORTED — always non-transactional
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * NOT_SUPPORTED:
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction exists → SUSPEND it.
     * • Always runs WITHOUT a transaction.
     *
     * CONSEQUENCE:
     *   The audit log is written outside any transaction context.
     *   It auto-commits immediately. If the outer transaction later
     *   rolls back, the audit log is NOT affected.
     *
     * USE WHEN: Long-running read operations where you don't want to
     *           hold a transaction open (e.g., generating reports,
     *           calling external APIs).
     *
     * CAUTION: Since there is no transaction, there is no rollback
     *          protection. Partial writes are possible.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void innerNotSupported(String action, boolean shouldFail) {
        logTransactionStatus("NOT_SUPPORTED");
        saveAuditLog(action, "NOT_SUPPORTED");
        if (shouldFail) {
            throw new RuntimeException("[NOT_SUPPORTED] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 5. MANDATORY — caller MUST provide a transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * MANDATORY:
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction exists → JOIN it.
     * • If NO transaction exists → throw IllegalTransactionStateException.
     *
     * CONSEQUENCE:
     *   This enforces that the method is NEVER called standalone — it
     *   must always be part of a larger transactional workflow.
     *
     * USE WHEN: Business logic that should only execute within an
     *           existing transaction boundary, e.g., a domain service
     *           method that must always be called from a @Transactional
     *           facade/orchestrator.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void innerMandatory(String action, boolean shouldFail) {
        logTransactionStatus("MANDATORY");
        saveAuditLog(action, "MANDATORY");
        if (shouldFail) {
            throw new RuntimeException("[MANDATORY] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 6. NEVER — caller must NOT have a transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * NEVER:
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction exists → throw IllegalTransactionStateException.
     * • If NO transaction exists → run without transaction (OK).
     *
     * CONSEQUENCE:
     *   The opposite of MANDATORY. This prevents accidental transactional
     *   calls to operations that must not participate in a transaction.
     *
     * USE WHEN: Operations that must happen outside any transaction,
     *           e.g., sending emails, making HTTP calls, or cache
     *           invalidation that should not be tied to DB commit.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.NEVER)
    public void innerNever(String action, boolean shouldFail) {
        logTransactionStatus("NEVER");
        // Cannot use JPA repo here if no transaction — just log
        log.info("[NEVER] Would save audit log for action='{}', but no Tx available for JPA", action);
        if (shouldFail) {
            throw new RuntimeException("[NEVER] Simulated failure in inner method");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 7. NESTED — savepoint within existing transaction
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * NESTED:
     * ────────────────────────────────────────────────────────────────────
     * • If a transaction exists → create a SAVEPOINT within it.
     *   If the inner method fails, only rolls back to the savepoint;
     *   the outer transaction can continue.
     * • If NO transaction exists → behaves like REQUIRED (creates new).
     *
     * CONSEQUENCE:
     *   Partial rollback is possible: the outer method can catch the
     *   exception from the inner method and decide to continue.
     *   The inner changes are rolled back, but outer changes survive.
     *
     * IMPORTANT — HIBERNATE / JPA LIMITATION:
     *   JpaTransactionManager supports NESTED only when
     *   {@code nestedTransactionAllowed} is set to true AND the JDBC
     *   driver supports savepoints (PostgreSQL does). However, Hibernate
     *   Session does not natively support savepoints. If you get
     *   NestedTransactionNotSupportedException, you may need to use
     *   JdbcTemplate or DataSourceTransactionManager instead.
     *
     * USE WHEN: You want partial rollback — e.g., processing a batch
     *           where individual item failures should not abort the
     *           entire batch.
     * ────────────────────────────────────────────────────────────────────
     */
    @Transactional(propagation = Propagation.NESTED)
    public void innerNested(String action, boolean shouldFail) {
        logTransactionStatus("NESTED");
        saveAuditLog(action, "NESTED");
        if (shouldFail) {
            throw new RuntimeException("[NESTED] Simulated failure — only savepoint rolls back");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helper methods
    // ─────────────────────────────────────────────────────────────────────

    private void logTransactionStatus(String propagationType) {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        String txName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("[{}] Transaction active={}, name={}", propagationType, active, txName);
    }

    private void saveAuditLog(String action, String propagationType) {
        AuditLog auditLog = AuditLog.builder()
                .action(action)
                .entityName("PropagationDemo")
                .details("Propagation type: " + propagationType)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepo.saveAndFlush(auditLog);
        log.info("[{}] Audit log saved: id={}", propagationType, auditLog.getId());
    }
}

