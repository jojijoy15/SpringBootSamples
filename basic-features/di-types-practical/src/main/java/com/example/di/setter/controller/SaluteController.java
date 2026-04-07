package com.example.di.setter.controller;

import com.example.di.dto.Greetings;
import com.example.di.setter.service.SaluteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaluteController {

    private SaluteService saluteService;

    /*
    Note:
        Dependency is optional
        Dependency must be already instantiated
        Best practice over field based injection
        Support optional dependency
     */

    @Autowired
    public void setSaluteService(SaluteService saluteService) {
        this.saluteService = saluteService;
    }

    @GetMapping("/greet/setter/{username}")
    public ResponseEntity<Greetings> sayHello(@PathVariable("username") String userName) {
        Greetings greetings = saluteService.salute(userName);
        return ResponseEntity.ok(greetings);
    }
}
