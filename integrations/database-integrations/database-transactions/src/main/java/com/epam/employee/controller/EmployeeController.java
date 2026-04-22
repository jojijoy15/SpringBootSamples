package com.epam.employee.controller;

import com.epam.employee.dto.EmployeeDTO;
import com.epam.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("detail/{empID}")
    public ResponseEntity<EmployeeDTO> employeeDetails(@PathVariable int empID){

        EmployeeDTO employee = employeeService.findEmployeeById(empID);
        if (Objects.nonNull(employee)){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
    }

    @PostMapping("employee")
    public ResponseEntity<EmployeeDTO> createEmployees(@RequestBody EmployeeDTO employeeDTO){

        EmployeeDTO employee = employeeService.createEmployee(employeeDTO);
        if (Objects.nonNull(employee)){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
    }

    @PutMapping("employee")
    public ResponseEntity<EmployeeDTO> updateEmployees(@RequestBody EmployeeDTO employeeDTO){

        EmployeeDTO employee = employeeService.updateEmployee(employeeDTO);
        if (Objects.nonNull(employee)){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
    }
}
