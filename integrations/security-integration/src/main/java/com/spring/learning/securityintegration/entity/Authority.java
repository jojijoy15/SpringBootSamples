package com.spring.learning.securityintegration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema = "spring-security")
public class Authority {

  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE) // postgres
  private Long id;

  @Column(name = "user_role")
  private String userRole;

}
