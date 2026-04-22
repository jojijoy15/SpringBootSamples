package com.epam.employee.mapper;

import com.epam.employee.dto.DepartmentDTO;
import com.epam.employee.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    Department mapDepartmentDTOToDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO mapDepartmentToDepartmentDTO(Department department);


}
