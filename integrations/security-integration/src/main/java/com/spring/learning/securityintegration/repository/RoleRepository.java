package com.spring.learning.securityintegration.repository;

import com.spring.learning.securityintegration.entity.Authority;
import javax.management.relation.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Authority, Long> {

}
