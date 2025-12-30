package com.sample.service.impl;

import com.sample.dto.UserDto;
import com.sample.repository.SampleRepo;
import com.sample.service.contract.UserService;
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
