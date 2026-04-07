package com.example.di.resource.controller;

import com.example.di.dto.Greetings;
import com.example.di.order.service.Sender;
import com.example.di.resource.service.ResourceWaveService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceWaveSetterController {

    private ResourceWaveService resourceWaveService;
    private Sender sender;


    @Resource
    public void setResourceWaveService(ResourceWaveService resourceWaveService) {
        this.resourceWaveService = resourceWaveService;
    }

    @Resource
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @GetMapping("/greet/resource/setter/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = resourceWaveService.getGreetings(userName);
        return ResponseEntity.ok(greetings);
    }
}
