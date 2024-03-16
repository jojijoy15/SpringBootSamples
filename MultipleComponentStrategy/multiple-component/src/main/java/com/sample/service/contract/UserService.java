package com.sample.service.contract;

import com.sample.dto.UserDto;
import java.util.List;

public interface UserService {

  List<UserDto> fetchUserByDetails(String detail);

}
