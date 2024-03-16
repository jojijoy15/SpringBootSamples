package com.sample.controller;

import com.sample.dto.UserDto;
import com.sample.service.strategy.ServiceSelectionStrategy;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final ServiceSelectionStrategy serviceSelectionStrategy;

  public UserController(final ServiceSelectionStrategy selectionStrategy) {
    this.serviceSelectionStrategy = selectionStrategy;
  }

  @GetMapping("/v1/user/age/{age}")
  public ResponseEntity<List<UserDto>> getUserByAge(@PathVariable("age") String age) {
    return ResponseEntity.ok(serviceSelectionStrategy.selectUserService(age).fetchUserByDetails(age));
  }

  @GetMapping("/v1/user/name/{name}")
  public ResponseEntity<List<UserDto>> getUserByName(@PathVariable("name") String name) {
    return ResponseEntity.ok(serviceSelectionStrategy.selectUserService(name).fetchUserByDetails(name));
  }
}
