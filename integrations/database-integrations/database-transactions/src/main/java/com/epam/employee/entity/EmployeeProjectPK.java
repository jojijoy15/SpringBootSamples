package com.epam.employee.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeProjectPK implements Serializable {

    private Integer empId;
    private Integer projectId;
}