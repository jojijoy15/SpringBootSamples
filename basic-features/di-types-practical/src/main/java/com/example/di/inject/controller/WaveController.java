package com.example.di.inject.controller;

import com.example.di.dto.Greetings;
import com.example.di.inject.service.WaveService;
import com.example.di.order.service.Sender;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaveController {

    private WaveService waveService;
    private  Sender sender;

    /*
    Note:
        @Inject [JSR-330] supports constructor based dependency injection
        Precedence:
            1. Match by Type
            2. Match by Qualifier (@Qualifier)
            3. Match by Name (@Named)

     */

    public WaveController(WaveService waveService) {
        this.waveService = waveService;
    }

    @Inject
    public WaveController(WaveService waveService, Sender sender) {
        this.waveService = waveService;
        this.sender = sender;
    }

    @GetMapping("/greet/inject/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = waveService.getGreetings(userName);
        return ResponseEntity.ok(greetings);
    }
}
