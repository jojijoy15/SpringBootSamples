package com.spring.learning.securityintegration.security.userdetails;

import com.spring.learning.securityintegration.entity.PlatformUser;
import com.spring.learning.securityintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlatformUserDetailsService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final PlatformUser userByUserName = repository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("PlatformUser with username does not exist"));
    return User.withUsername(userByUserName.getUserName())
        .password(userByUserName.getPassword())
        .authorities(userByUserName.getUserRole().getUserRole())
        .build();
  }
}
