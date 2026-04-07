package com.example.di.resource.controller;

import com.example.di.dto.Greetings;
import com.example.di.order.service.Sender;
import com.example.di.resource.service.ResourceWaveService;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;

@RestController
public class ResourceWaveController {

    @Resource
    private ResourceWaveService resourceWaveService;

    @Resource
    private Sender sender;

    /*
    Note:
        @Resource [JSR-250] annotation is not supported on top of constructor
        Precedence:
            1. Match by Name (@Resource(name=<value>)) or @Bean(value=<value>)
            2. Match by Type
            3. Match by Qualifier (@Qualifier)

    @Resource
    public ResourceWaveController(ResourceWaveService resourceWaveService, Sender sender) {
        this.resourceWaveService = resourceWaveService;
        this.sender = sender;
    }
    */

    @Bean
    @GetMapping("/greet/resource/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = resourceWaveService.getGreetings(userName);
        return ResponseEntity.ok(greetings);
    }
}
