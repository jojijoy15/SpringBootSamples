package com.learning.spring.service.strategy;

import com.learning.spring.service.contract.UserService;
import com.learning.spring.service.impl.UserDetailByAge;
import com.learning.spring.service.impl.UserDetailByName;
import java.beans.Introspector;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ServiceSelectionStrategy {

  private final Map<String, UserService> userServiceMap;

  public ServiceSelectionStrategy(final Map<String, UserService> userServiceMap) {
    this.userServiceMap = userServiceMap;
  }

  public UserService selectUserService(String userDetail) {
    String serviceName = "";
    if(userDetail.matches("\\d{1,3}"))
      serviceName = UserDetailByAge.class.getSimpleName();
    else
      serviceName = UserDetailByName.class.getSimpleName();
    return userServiceMap.get(Introspector.decapitalize(serviceName));
  }
}
