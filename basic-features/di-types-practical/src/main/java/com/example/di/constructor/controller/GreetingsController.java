package com.example.di.constructor.controller;

import com.example.di.constructor.service.GreetingService;
import com.example.di.dto.Greetings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    private final GreetingService englishGreetingService;

    /*
    Note:
        Dependency is mandatory
        Dependency must be already instantiated
        Best practice over field based injection
        Does not support optional dependency
     */
    public GreetingsController(GreetingService greetingsService) {
        this.englishGreetingService = greetingsService;
    }

    @GetMapping("/greet/constructor/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = englishGreetingService.getGreetings(userName);
        return ResponseEntity.ok(greetings);
    }
}
