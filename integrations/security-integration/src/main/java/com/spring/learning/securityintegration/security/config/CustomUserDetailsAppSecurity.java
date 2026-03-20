package com.spring.learning.securityintegration.security.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;

/*
  * Custom database based user details service
*/
@Profile("database")
@Configuration
public class CustomUserDetailsAppSecurity {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    //cors configuration
    httpSecurity.cors(corsConfigurer -> {
      corsConfigurer.configurationSource((corsConfigSrc) -> {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        return configuration;
      });
    });
    httpSecurity.authorizeHttpRequests(
        requests -> requests
        .requestMatchers("/v1/greet/**").authenticated()
        .requestMatchers("/login", "/error").permitAll());
    httpSecurity.formLogin(
  flc -> flc
            .defaultSuccessUrl("/v1/greet/1")
    );
    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  CompromisedPasswordChecker passwordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
  }
}
