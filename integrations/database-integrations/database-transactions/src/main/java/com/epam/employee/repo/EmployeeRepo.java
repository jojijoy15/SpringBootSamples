package com.epam.employee.repo;

import com.epam.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    /** Count employees belonging to a department — used in phantom read demo. */
    long countByDepartment_DeptId(Integer deptId);
}
