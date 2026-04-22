package com.epam.employee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryHistoryDTO {

    private Integer salId;
    private Integer empId;
    private BigDecimal salary;
    private LocalDate fromDate;
    private LocalDate toDate;
}