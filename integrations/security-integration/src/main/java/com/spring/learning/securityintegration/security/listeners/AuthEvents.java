package com.spring.learning.securityintegration.security.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEvents {

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("Authentication Success");
    }

    @EventListener
    public void onAuthenticationSuccess(AbstractAuthenticationFailureEvent event) {
        log.info("Authentication Failure");
    }
}
