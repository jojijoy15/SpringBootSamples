package com.spring.learning.securityintegration.security.config;

import jakarta.annotation.Generated;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class AppSecurity {

  @Bean
  @Generated()
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    //session management
    httpSecurity
        .securityContext(sc -> sc.requireExplicitSave(false))
        .sessionManagement(sc -> sc
          .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.newSession())
          .invalidSessionUrl("invalidSession")
          .maximumSessions(2).maxSessionsPreventsLogin(true)
          .expiredUrl("/expired")
      );

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
      httpSecurity.authorizeHttpRequests(requests -> requests.requestMatchers("/**").authenticated());
      return httpSecurity.build();
  }

  @Bean
  @EventListener
  UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
    /*
      In Memory database
    */
    return new JdbcUserDetailsManager(dataSource);

  }


  // @Bean
  UserDetailsService inMemoryUserDetailsService() {
    /*
      In Memory database
    */
    UserDetails user = User.withUsername("user")
        .password("helloworld").build();
    return new InMemoryUserDetailsManager(user);

  }

  //@Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  //@Bean
  CompromisedPasswordChecker passwordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
  }
}
