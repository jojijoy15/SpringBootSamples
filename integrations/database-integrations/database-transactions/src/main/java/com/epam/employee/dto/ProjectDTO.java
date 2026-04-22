package com.epam.employee.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Integer projectId;
    private String projectName;
    private Integer deptId;
    private LocalDate startDate;
    private LocalDate endDate;
}