package com.sample.service.impl;

import com.sample.dto.UserDto;
import com.sample.repository.SampleRepo;
import com.sample.service.contract.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserDetailByName implements UserService {

  private final SampleRepo sampleRepo;

  public UserDetailByName(final SampleRepo sampleRepo) {
    this.sampleRepo = sampleRepo;
  }

  @Override
  public List<UserDto> fetchUserByDetails(String detail) {
    return sampleRepo.getUserByName(detail);
  }
}
