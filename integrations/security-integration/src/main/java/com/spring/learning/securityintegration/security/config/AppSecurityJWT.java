package com.spring.learning.securityintegration.security.config;

import com.spring.learning.securityintegration.security.filters.JwtTokenGenerator;
import com.spring.learning.securityintegration.security.filters.JwtTokenValidator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.tomcat.websocket.WsContainerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
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
