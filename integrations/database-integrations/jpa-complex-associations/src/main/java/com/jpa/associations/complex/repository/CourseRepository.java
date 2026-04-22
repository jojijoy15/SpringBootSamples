package com.jpa.associations.complex.repository;

import com.jpa.associations.complex.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
