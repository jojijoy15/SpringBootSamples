package com.example.beanscopes.session;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
//@Scope(scopeName = SCOPE_SESSION)
public class RequestSession {

    String userName;

    RequestSession() {
        this.userName = new Faker().funnyName().name();
    }

    public String getUserName() {
        return userName;
    }
}
