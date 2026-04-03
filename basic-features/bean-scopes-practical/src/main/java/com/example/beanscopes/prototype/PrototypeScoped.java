package com.example.beanscopes.prototype;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(scopeName = SCOPE_PROTOTYPE)
public class PrototypeScoped {

    private static int counter = 0;

    public PrototypeScoped(){
        counter++;
    }

    public String getCounter() {
        return "Created prototype scoped object count : " + counter;
    }
}
