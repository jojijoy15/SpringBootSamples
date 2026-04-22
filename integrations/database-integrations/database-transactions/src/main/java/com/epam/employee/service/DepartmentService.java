package com.epam.employee.service;

import com.epam.employee.dto.DepartmentDTO;
import com.epam.employee.entity.Department;

public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO fetchDepartmentDetails(int deptId);
    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO);
}
