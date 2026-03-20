package com.spring.learning.securityintegration.security.config;

import com.spring.learning.securityintegration.security.filters.JwtTokenGenerator;
import com.spring.learning.securityintegration.security.filters.JwtTokenValidator;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Profile("jwt")
@Configuration
public class AppSecurityJWT {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    //session management
    httpSecurity.sessionManagement(sc ->
           sc.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    //cors configuration
    httpSecurity.cors(corsConfigurer -> {
      corsConfigurer.configurationSource((corsConfigSrc) -> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(List.of("http://localhost:4200"));
          configuration.setAllowedHeaders(List.of("*"));
          configuration.setAllowedMethods(List.of("*"));
          configuration.setAllowCredentials(true);
          configuration.setMaxAge(3600L);
          configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
          return configuration;
      });
    });
    httpSecurity
        .addFilterAfter(new JwtTokenGenerator(), BasicAuthenticationFilter.class)
        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class);

    httpSecurity.authorizeHttpRequests(requests -> requests.requestMatchers("/**").authenticated());
    return httpSecurity.build();
  }


}
