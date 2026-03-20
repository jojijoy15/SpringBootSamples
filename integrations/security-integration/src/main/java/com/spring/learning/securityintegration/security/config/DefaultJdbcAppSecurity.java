package com.spring.learning.securityintegration.security.config;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

/*
 * Inbuilt jdbc based user details service
*/

@Profile("jdbc")
@Configuration
public class DefaultJdbcAppSecurity {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    //HTTP[s] protocol configuration
    //httpSecurity
        //ensures request is always in https protocol
        //.requiresChannel(config -> config.anyRequest().requiresSecure());
        //ensures request is always in *http* protocol
        //.requiresChannel(config -> config.anyRequest().requiresInsecure());

    //session management
    httpSecurity
        .securityContext(sc -> sc.requireExplicitSave(false))
        // configure session management
        .sessionManagement(sc -> sc
            /*
              Configure session to avoid session fixation attack
                - Change Session id (default), create a new session id with existing session details
                - new session, always create a new session with no existing details, only spring security config will be copied
                - migrate session, creates a new session with existing session details copied
                - none, disable it
             */
            .sessionFixation(sessionFixationConfigurer -> sessionFixationConfigurer.newSession())
            // URL to redirect to when Session becomes invalid
            .invalidSessionUrl("/invalidSession")
            //Max concurrent session a user can have
            .maximumSessions(2)
            //Prevent login if already max session is breached
            .maxSessionsPreventsLogin(true) //if false (default), previous or already logged in session will be stopped
            //Redirect URL, if a user tries to access a resource &
            // their session has expired due to too many sessions
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
      httpSecurity.authorizeHttpRequests(requests ->
           requests.requestMatchers("/login").permitAll()
           .requestMatchers("/greet/**").authenticated()
      );

      //Configure custom authentication entry point
//      httpSecurity.httpBasic(basic -> basic.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
      //Configure global exception handling for security
//      httpSecurity.exceptionHandling(
//          config -> config
//               .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//               .accessDeniedHandler(new CustomAccessDeniedExceptionHandler())
//      );
      httpSecurity
//          .httpBasic(Customizer.withDefaults())
//          .formLogin(Customizer.withDefaults());
          .formLogin(flc ->
              flc.defaultSuccessUrl("/v1/greet/1"));
      return httpSecurity.build();
  }

  /*
    Only one bean of PlatformUser Detail service should exist
  */
  //@Bean
  UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
    /*
      JDBC PlatformUser Details Manager is out of box user details manager
      This provides a predefined database schema for user management.
      This would need JDBC API dependency
    */
    return new JdbcUserDetailsManager(dataSource);
  }


  @Bean
  UserDetailsService inMemoryUserDetailsService() {
    /*
      In Memory database
    */
    UserDetails user = User.withUsername("user")
        .password("{noop}helloworld").build();
    return new InMemoryUserDetailsManager(user);
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
