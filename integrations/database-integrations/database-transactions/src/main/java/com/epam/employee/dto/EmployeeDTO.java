package com.epam.employee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Integer empId;
    private String empName;
    private String email;
    private BigDecimal salary;
    private Integer deptId;
    private Integer managerId;
    private LocalDate joinDate;
    private String location;
    private Integer version;
}