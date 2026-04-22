package com.jpa.associations.complex.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CompositeCourseStudentKey {

  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "student_id")
  private Integer studentId;


}
