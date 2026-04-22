package com.epam.employee.mapper;

import com.epam.employee.dto.EmployeeDTO;
import com.epam.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    Employee mapEmployeeDTOToEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee);
}
