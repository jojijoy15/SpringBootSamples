package com.learning.spring.service.impl;

import com.learning.spring.dto.UserDto;
import com.learning.spring.repository.SampleRepo;
import com.learning.spring.service.contract.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserDetailByAge implements UserService {

  private final SampleRepo sampleRepo;

  public UserDetailByAge(final SampleRepo sampleRepo) {
    this.sampleRepo = sampleRepo;
  }

  @Override
  public List<UserDto> fetchUserByDetails(String detail) {
    return sampleRepo.getUserByAge(Integer.valueOf(detail));
  }
}
