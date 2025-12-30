package com.client.app.config;

import java.util.Map;
import java.util.Optional;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class AppNameConfig {

  private final Map<String, String> property;

  public AppNameConfig(Map<String, String> property) {
    this.property = property;
  }

  public String getAppName() {
    return Optional.ofNullable(property.get("value")).orElse("dummy Name");
  }

}
