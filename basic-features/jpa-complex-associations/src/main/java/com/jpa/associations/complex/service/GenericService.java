package com.jpa.associations.complex.service;

import com.jpa.associations.complex.entity.Course;
import com.jpa.associations.complex.repository.CourseRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class GenericService {

  private final CourseRepository repository;

  public List<Course> getAllCourses() {
    final List<Course> all = repository.findAll();
    all.forEach((e) -> e.getRating());
    return all;
  }
}
