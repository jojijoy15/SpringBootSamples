package com.example.di.inject.controller;

import com.example.di.dto.Greetings;
import com.example.di.inject.service.WaveService;
import com.example.di.order.service.Sender;
import jakarta.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaveSetterController {

    private WaveService waveService;
    private  Sender sender;

    @Inject
    public void setWaveService(WaveService waveService) {
        this.waveService = waveService;
    }

    @Inject
    public void setSender(Sender sender) {
        this.sender = sender;
    }


    @GetMapping("/greet/inject/setter/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = waveService.getGreetings(userName);
        return ResponseEntity.ok(greetings);
    }
}
