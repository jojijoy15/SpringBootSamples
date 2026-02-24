package com.jpa.associations.complex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "college")
@Getter
@Setter
public class Student {

  @Id
  private Integer id;
  private String name;

//  @ManyToMany
//  @JoinTable(
//      schema = "college",
//      name = "course_student_link",
//      joinColumns = @JoinColumn(name = "student_id"),
//      inverseJoinColumns = @JoinColumn(name = "course_id")
//  )
//  @JsonIgnore // Important else will keep on going in a cycle
//  private Set<Course> courses;

  @OneToMany(mappedBy = "student")
  private Set<CourseRating> rating;

}
