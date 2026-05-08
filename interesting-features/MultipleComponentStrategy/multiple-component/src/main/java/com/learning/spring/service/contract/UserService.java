package com.learning.spring.service.contract;

import com.learning.spring.dto.UserDto;
import java.util.List;

public interface UserService {

  List<UserDto> fetchUserByDetails(String detail);

}
