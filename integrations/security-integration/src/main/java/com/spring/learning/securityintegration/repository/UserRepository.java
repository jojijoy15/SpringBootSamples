package com.spring.learning.securityintegration.repository;

import com.spring.learning.securityintegration.entity.PlatformUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<PlatformUser, Long> {

  Optional<PlatformUser> findByUserName(String userName);
}
