package com.epam.employee.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Integer deptId;
    private String deptName;
    private BigDecimal budget;
}