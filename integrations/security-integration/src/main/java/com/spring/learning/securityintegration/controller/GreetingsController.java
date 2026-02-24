package com.spring.learning.securityintegration.controller;

import com.spring.learning.securityintegration.model.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class GreetingsController {

  @GetMapping("/greet/{user}")
  public ResponseEntity<Greeting> greetUser(@PathVariable("user") String user) {
    Greeting greeting = new Greeting("Good Morning", user);
    return ResponseEntity.ok(greeting);
  }

}
