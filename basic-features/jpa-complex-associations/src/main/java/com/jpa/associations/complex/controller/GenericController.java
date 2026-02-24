package com.jpa.associations.complex.controller;

import com.jpa.associations.complex.entity.Course;
import com.jpa.associations.complex.service.GenericService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1")
public class GenericController {

  private final GenericService service;

  @GetMapping("/courses")
  public ResponseEntity<List<Course>> getAllCourses() {
    return ResponseEntity.ok(service.getAllCourses());
  }

}
