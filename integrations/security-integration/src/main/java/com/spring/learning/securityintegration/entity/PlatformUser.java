package com.spring.learning.securityintegration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema = "spring-security", name = "users")
public class PlatformUser {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long userId;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "password")
  private String password;

  @ManyToOne
  @JoinColumn(name = "user_role")
  private Authority authority;

}
