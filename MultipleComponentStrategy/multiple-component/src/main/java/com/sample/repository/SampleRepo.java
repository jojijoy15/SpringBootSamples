package com.sample.repository;

import com.sample.dto.UserDto;
import com.sample.model.User;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SampleRepo {

  private static final List<User> inMemDataStore =
      List.of(new User(1L, 18, "user1"),
              new User(2L, 20, "user1"),
              new User(3L, 23, "user2"));

  public List<UserDto> getUserByName(String name) {
    return inMemDataStore.stream()
        .filter(user -> user.name().equalsIgnoreCase(name))
        .map(user -> new UserDto(user.userId(), user.age(), user.name()))
        .toList();
  }

  public List<UserDto> getUserByAge(Integer age) {
    return inMemDataStore.stream()
        .filter(user -> user.age().equals(age))
        .map(user -> new UserDto(user.userId(), user.age(), user.name()))
        .toList();
  }

}
