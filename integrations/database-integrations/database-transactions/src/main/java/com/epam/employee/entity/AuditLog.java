package com.epam.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ═══════════════════════════════════════════════════════════════════════
 * AUDIT_LOG — secondary table used in PROPAGATION demos.
 * ═══════════════════════════════════════════════════════════════════════
 * This table is intentionally simple — it exists to demonstrate how
 * different propagation types affect commit/rollback across beans.
 *
 * Example: with REQUIRES_NEW the audit log is committed even when
 * the outer (calling) transaction rolls back.
 * ═══════════════════════════════════════════════════════════════════════
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** e.g. "CREATE", "UPDATE", "DELETE" */
    @Column(nullable = false)
    private String action;

    /** e.g. "Employee", "Department" */
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    /** PK of the affected entity */
    @Column(name = "entity_id")
    private Integer entityId;

    /** Free-text details about what happened */
    @Column(length = 1000)
    private String details;

    /** When the action was recorded */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}

