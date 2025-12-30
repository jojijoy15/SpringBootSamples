package com.client.app.controller;

import com.client.app.config.AppNameConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

  private final AppNameConfig appNameConfig;
  private static final Logger logger = LogManager.getLogger(AppController.class);

  public AppController(AppNameConfig appNameConfig) {
    this.appNameConfig = appNameConfig;
  }

  @GetMapping("/appName")
  public ResponseEntity<String> getAppName() {
    logger.log(Level.INFO, "Request received");
    return ResponseEntity.ok(appNameConfig.getAppName());
  }

}
