package com.example.componentscan.singleton;

import org.springframework.stereotype.Component;

@Component // default singleton scoped
public class SingletonScoped {

    private static int counter = 0;

    public SingletonScoped () {
        this.counter++;
    }
    public String doSomething() {
        return "Created singleton scoped object count : " + counter;
    }
}
