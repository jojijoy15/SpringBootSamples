package com.jpa.associations.complex.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rating", schema = "college")
@Getter
@Setter
public class CourseRating {

  @EmbeddedId
  private CompositeCourseStudentKey id;

  @MapsId("courseId")
  @JoinColumn(name = "course_id")
  @ManyToOne
  private Course course;

  @MapsId("studentId")
  @JoinColumn(name = "student_id")
  @ManyToOne
  private Student student;

  private Integer rating;
}
