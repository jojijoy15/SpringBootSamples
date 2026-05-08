package com.spring.learning.securityintegration.security.config;

import com.spring.learning.securityintegration.security.filters.JwtTokenGenerator;
import com.spring.learning.securityintegration.security.filters.JwtTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@Profile("jwt")
public class JWTSecurityConfig {

    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http){
        http.sessionManagement(sessionConfig -> {
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //No session context storage needed
        });
        http.cors(config -> {
            config.configurationSource(request -> {
                var corsConfiguration =  new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowedHeaders(List.of("*"));
                corsConfiguration.setAllowCredentials(false); // true for allowing credentials, does not work, when allowed origin is *, not needed for stateless JWT auth
                corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
                return corsConfiguration;
            });
        });

        http
            .csrf(csrf -> csrf.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
            .ignoringRequestMatchers("/uri1", "uri2")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        );
        http.addFilterAfter(new JwtTokenGenerator(), BasicAuthenticationFilter.class)
            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class);
        http.redirectToHttps(Customizer.withDefaults());
        //Endpoint config
        return http.build();
    }
}
