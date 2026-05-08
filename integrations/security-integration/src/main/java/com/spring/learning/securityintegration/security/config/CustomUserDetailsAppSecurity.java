package com.spring.learning.securityintegration.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;

/*
  * Custom database based user details service
*/
@Profile("database")
@Configuration
public class CustomUserDetailsAppSecurity {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

    httpSecurity.securityContext(
            context -> context.requireExplicitSave(false) // Configure explicit save of session for this app
    );
    httpSecurity.sessionManagement(sessionConfig -> {
      sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // create session and manage it for this app
    });
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
//        .requestMatchers("/v1/greet/**").authenticated()
        .requestMatchers("/v1/greet/**").hasAuthority("USER")
        /*
            // Access method example
             .requestMatchers("/v1/greet/**")
             .access(new WebExpressionAuthorizationManager("hasRole('USER') AND hasRole('ADMIN')"))
             //.access(allOf(hasAuthority("admin"), hasAuthority("user")))
             //.access(new WebExpressionAuthorizationManager("#name == authentication.name"))
        */
        .requestMatchers("/login", "/error", "/public").permitAll());
    httpSecurity
          //.csrf(AbstractHttpConfigurer::disable)  // Configuration disabled
        .csrf(csrfConfig ->  // applicable to POST PUT DELETE method
           csrfConfig
                   .ignoringRequestMatchers("/public")      //To ignore /public csrf token for public endpoint, must be permitAll in authorization request config
                  .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
          ).addFilterAfter(new CSRFTokenFilter(), BasicAuthenticationFilter.class); // explicitly mention this to trigger csrf token generation

    httpSecurity.formLogin(
  flc -> flc
            //.loginPage("/login")                             //Customize Login Page
            //.usernameParameter("userId")                     //Customize username parameter to userId, default is username
            //.passwordParameter("pwd")                        //Customize password parameter to pwd, default is password
          .defaultSuccessUrl("/v1/greet/1")                    //Customization on successful login
          //.successHandler()                                  //Customization success handler
          //.failureHandler()                                  //Customization failure handler
    );
    /*
    httpSecurity.logout(config -> {
      config.logoutUrl("/logout")                              //logout url for logout
              .invalidateHttpSession(true)                     //invalidate session
              .deleteCookies("JSESSIONID")                     //delete cookies
              .clearAuthentication(true)
              .logoutSuccessUrl("/login?logout").permitAll()   //logout success url
    });
    */
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
