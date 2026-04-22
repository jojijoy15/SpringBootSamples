package com.epam.employee.controller;

import com.epam.employee.dto.DepartmentDTO;
import com.epam.employee.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/department")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO){

        DepartmentDTO department = departmentService.createDepartment(departmentDTO);
        if (Objects.nonNull(department)){
            return new ResponseEntity<>(department, HttpStatus.OK);
        }
        return new ResponseEntity<>(department, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/department")
    public ResponseEntity<DepartmentDTO> updateDepartment(@RequestBody DepartmentDTO departmentDTO){

        DepartmentDTO department = departmentService.updateDepartment(departmentDTO);
        if (Objects.nonNull(department)){
            return new ResponseEntity<>(department, HttpStatus.OK);
        }
        return new ResponseEntity<>(department, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/detail/{deptId}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable int deptId){

        DepartmentDTO department = departmentService.fetchDepartmentDetails(deptId);
        if (Objects.nonNull(department)){
            return new ResponseEntity<>(department, HttpStatus.OK);
        }
        return new ResponseEntity<>(department, HttpStatus.NOT_FOUND);
    }


}
