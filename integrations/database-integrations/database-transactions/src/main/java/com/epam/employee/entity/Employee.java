package com.epam.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "emp_name", nullable = false)
    private String empName;

    @Column(unique = true)
    private String email;

    private BigDecimal salary;

    @Column(name = "join_date")
    private LocalDate joinDate;

    private String location;

    /**
     * ──────────────────────────────────────────────────────────────────────
     * OPTIMISTIC LOCKING — @Version
     * ──────────────────────────────────────────────────────────────────────
     * Hibernate automatically increments this value on every UPDATE.
     * If two transactions read the same version and both try to update,
     * the second one will get an OptimisticLockException — this prevents
     * the LOST UPDATE problem without using pessimistic (SELECT FOR UPDATE)
     * locking.
     * ──────────────────────────────────────────────────────────────────────
     */
    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> subordinates;

    @OneToMany(mappedBy = "employee")
    private List<SalaryHistory> salaryHistories;
}