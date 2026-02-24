package com.spring.learning.securityintegration.security.filters;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenGenerator extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(null != authentication) {
      final Environment environment = getEnvironment();
      String secretKey = environment.getProperty("JWT_SECRET_KEY", "samplesecretkey"); // Just for learning, should ideally come from vault
      SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
      String jwt = Jwts.builder().issuer("Some Issuer").subject("subject")
          .claim("username", authentication.getName())
          .claim("authorities",  "READ,WRITE")
          .issuedAt(new Date())
          .expiration(Date.from(Instant.now().plus(1000, ChronoUnit.SECONDS)))
          .signWith(key)
          .compact();
      response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getServletPath().equals("/user");
  }

}
