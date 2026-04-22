package com.epam.employee.controller;

import com.epam.employee.service.impl.IsolationDemoService;
import com.epam.employee.service.impl.PropagationOuterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * TransactionDemoController — REST endpoints for all @Transactional demos
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * PREREQUISITES:
 *   1. Start PostgreSQL:  docker compose up -d
 *   2. Create at least one Department and one Employee via the existing
 *      /departments and /employees endpoints (see DepartmentController
 *      and EmployeeController).
 *   3. Then call these demo endpoints to observe transactional behaviour.
 *
 * TIP: Watch the console logs — they show transaction begin/commit/rollback
 *      events because of the DEBUG logging enabled in application.yaml.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * ISOLATION LEVEL ENDPOINTS
 * ─────────────────────────
 *   GET /api/transaction-demos/isolation/dirty-read?empId=1
 *   GET /api/transaction-demos/isolation/non-repeatable-read?deptId=1
 *   GET /api/transaction-demos/isolation/repeatable-read?deptId=1
 *   GET /api/transaction-demos/isolation/serializable?deptId=1
 *   GET /api/transaction-demos/isolation/lost-update?empId=1
 *   GET /api/transaction-demos/isolation/pessimistic-locking?empId=1
 *
 * PROPAGATION ENDPOINTS
 * ─────────────────────
 *   GET /api/transaction-demos/propagation/required?empId=1&innerFails=false
 *   GET /api/transaction-demos/propagation/requires-new?empId=1&outerFails=false
 *   GET /api/transaction-demos/propagation/supports-with-tx
 *   GET /api/transaction-demos/propagation/supports-without-tx
 *   GET /api/transaction-demos/propagation/not-supported?empId=1
 *   GET /api/transaction-demos/propagation/mandatory-with-tx
 *   GET /api/transaction-demos/propagation/mandatory-without-tx
 *   GET /api/transaction-demos/propagation/never-with-tx
 *   GET /api/transaction-demos/propagation/never-without-tx
 *   GET /api/transaction-demos/propagation/nested?empId=1
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/api/transaction-demos")
public class TransactionDemoController {

    private final IsolationDemoService isolationDemoService;
    private final PropagationOuterService propagationOuterService;

    @Autowired
    public TransactionDemoController(IsolationDemoService isolationDemoService,
                                     PropagationOuterService propagationOuterService) {
        this.isolationDemoService = isolationDemoService;
        this.propagationOuterService = propagationOuterService;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ISOLATION LEVEL DEMOS
    // ═══════════════════════════════════════════════════════════════════════

    @GetMapping("/isolation/dirty-read")
    public ResponseEntity<Map<String, String>> dirtyRead(@RequestParam int empId) {
        return ResponseEntity.ok(isolationDemoService.demoDirtyRead(empId));
    }

    @GetMapping("/isolation/non-repeatable-read")
    public ResponseEntity<Map<String, String>> nonRepeatableRead(@RequestParam int deptId) {
        return ResponseEntity.ok(isolationDemoService.demoNonRepeatableRead(deptId));
    }

    @GetMapping("/isolation/repeatable-read")
    public ResponseEntity<Map<String, String>> repeatableRead(@RequestParam int deptId) {
        return ResponseEntity.ok(isolationDemoService.demoRepeatableRead(deptId));
    }

    @GetMapping("/isolation/serializable")
    public ResponseEntity<Map<String, String>> serializable(@RequestParam int deptId) {
        return ResponseEntity.ok(isolationDemoService.demoSerializable(deptId));
    }

    @GetMapping("/isolation/lost-update")
    public ResponseEntity<Map<String, String>> lostUpdate(@RequestParam int empId) {
        return ResponseEntity.ok(isolationDemoService.demoLostUpdate(empId));
    }

    @GetMapping("/isolation/pessimistic-locking")
    public ResponseEntity<Map<String, String>> pessimisticLocking(@RequestParam int empId) {
        return ResponseEntity.ok(isolationDemoService.demoPessimisticLocking(empId));
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PROPAGATION DEMOS
    // ═══════════════════════════════════════════════════════════════════════

    @GetMapping("/propagation/required")
    public ResponseEntity<Map<String, String>> required(
            @RequestParam int empId,
            @RequestParam(defaultValue = "false") boolean innerFails) {
        try {
            return ResponseEntity.ok(propagationOuterService.demoRequired(empId, innerFails));
        } catch (RuntimeException ex) {
            Map<String, String> result = new LinkedHashMap<>();
            result.put("outcome", "Transaction rolled back");
            result.put("exception", ex.getMessage());
            result.put("lesson", "REQUIRED: inner failure caused entire Tx to rollback " +
                    "because both share the same physical transaction.");
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/propagation/requires-new")
    public ResponseEntity<Map<String, String>> requiresNew(
            @RequestParam int empId,
            @RequestParam(defaultValue = "false") boolean outerFails) {
        try {
            return ResponseEntity.ok(propagationOuterService.demoRequiresNew(empId, outerFails));
        } catch (RuntimeException ex) {
            Map<String, String> result = new LinkedHashMap<>();
            result.put("outcome", "Outer transaction rolled back, but audit log SURVIVED");
            result.put("exception", ex.getMessage());
            result.put("lesson", "REQUIRES_NEW: inner's Tx was committed independently. " +
                    "The audit log persists even though the outer Tx rolled back.");
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/propagation/supports-with-tx")
    public ResponseEntity<Map<String, String>> supportsWithTx() {
        return ResponseEntity.ok(propagationOuterService.demoSupportsWithTx());
    }

    @GetMapping("/propagation/supports-without-tx")
    public ResponseEntity<Map<String, String>> supportsWithoutTx() {
        return ResponseEntity.ok(propagationOuterService.demoSupportsWithoutTx());
    }

    @GetMapping("/propagation/not-supported")
    public ResponseEntity<Map<String, String>> notSupported(@RequestParam int empId) {
        return ResponseEntity.ok(propagationOuterService.demoNotSupported(empId));
    }

    @GetMapping("/propagation/mandatory-with-tx")
    public ResponseEntity<Map<String, String>> mandatoryWithTx() {
        return ResponseEntity.ok(propagationOuterService.demoMandatoryWithTx());
    }

    @GetMapping("/propagation/mandatory-without-tx")
    public ResponseEntity<Map<String, String>> mandatoryWithoutTx() {
        return ResponseEntity.ok(propagationOuterService.demoMandatoryWithoutTx());
    }

    @GetMapping("/propagation/never-with-tx")
    public ResponseEntity<Map<String, String>> neverWithTx() {
        try {
            return ResponseEntity.ok(propagationOuterService.demoNeverWithTx());
        } catch (Exception ex) {
            Map<String, String> result = new LinkedHashMap<>();
            result.put("outcome", "Exception thrown");
            result.put("exception", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            result.put("lesson", "NEVER: calling within a Tx throws IllegalTransactionStateException.");
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/propagation/never-without-tx")
    public ResponseEntity<Map<String, String>> neverWithoutTx() {
        return ResponseEntity.ok(propagationOuterService.demoNeverWithoutTx());
    }

    @GetMapping("/propagation/nested")
    public ResponseEntity<Map<String, String>> nested(@RequestParam int empId) {
        try {
            return ResponseEntity.ok(propagationOuterService.demoNested(empId));
        } catch (Exception ex) {
            Map<String, String> result = new LinkedHashMap<>();
            result.put("outcome", "Exception: " + ex.getClass().getSimpleName());
            result.put("message", ex.getMessage());
            result.put("lesson", "NESTED may not be supported by JPA/Hibernate. " +
                    "You may see NestedTransactionNotSupportedException. " +
                    "Use DataSourceTransactionManager + JdbcTemplate for NESTED support.");
            return ResponseEntity.ok(result);
        }
    }
}

