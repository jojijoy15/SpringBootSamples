package com.example.beanscopes.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Map;

@Component
@ApplicationScope
//@Scope(scopeName = SCOPE_APPLICATION)
public class ApplicationScoped {

    private static int counter;

    @Autowired
    private Map<String, ApplicationContext> applicationContexts;

    public ApplicationScoped() {
        counter++;
    }

    public String getCounter() {
        return "Created application scoped object count : " + counter;
    }

}
