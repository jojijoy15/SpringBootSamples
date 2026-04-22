package com.epam.employee.service;

import com.epam.employee.dto.EmployeeDTO;

public interface EmployeeService {

    EmployeeDTO findEmployeeById(int empId);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO);
}
