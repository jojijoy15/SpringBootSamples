package com.spring.learning.securityintegration.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
      log.error("Authentication Exception : {}", authException);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }
}
