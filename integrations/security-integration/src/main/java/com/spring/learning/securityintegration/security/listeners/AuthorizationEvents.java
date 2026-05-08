package com.spring.learning.securityintegration.security.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent<String> event) {

        // Only publishes failure events, success events i.e., Granted events it is not enabled by default
        // Authorization events are quite noisy. Can be done for admin roles though
        log.info("Authorization Events : {}", event);

    }

}
