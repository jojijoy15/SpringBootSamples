package com.epam.employee.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectDTO {

    private Integer empId;
    private Integer projectId;
    private String role;
    private Integer allocation;
}