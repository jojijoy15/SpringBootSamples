package com.jpa.associations.complex.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(schema = "college")
@Getter
@Setter
public class Course {

  @Id
  private Integer id;
  private String name;

//  @ManyToMany(mappedBy = "courses")  // Note: removed as using composite key
//  private Set<Student> student;

  @OneToMany(mappedBy = "course")
  private Set<CourseRating> rating;
}
