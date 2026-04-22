package com.epam.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMP_PROJECT")
public class EmployeeProject {

    @EmbeddedId
    private EmployeeProjectPK id;

    @ManyToOne
    @MapsId("empId")
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    private String role;

    private Integer allocation;
}