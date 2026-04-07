package com.example.di.field.controller;

import com.example.di.dto.Greetings;
import com.example.di.field.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired(required = false)
    private HelloService helloService;

    /*
    Note:
        Dependency is mandatory, by default. Unless specified with required false
        Dependency must be already instantiated
        Discouraged over constructor DI, unless just for testing.

        Precedence
            1. Match by Type
            2. Match by Qualifier (@Qualifier)
            3. Match by Name (by field Name)
     */

    @GetMapping("/greet/field/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = helloService.sayHello(userName);
        return ResponseEntity.ok(greetings);
    }
}
