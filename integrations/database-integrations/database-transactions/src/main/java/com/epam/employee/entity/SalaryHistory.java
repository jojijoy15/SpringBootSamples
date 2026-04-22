package com.epam.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SALARY_HISTORY")
public class SalaryHistory {

    @Id
    @Column(name = "sal_id")
    private Integer salId;

    private BigDecimal salary;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;
}