package com.example.beanscopes.application;

import com.example.beanscopes.order.Sender;
import com.example.beanscopes.prototype.PrototypeScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationScoped applicationScoped;

    @Autowired
    private Sender sender;


    @GetMapping("/application")
    public ResponseEntity<String> getCount(PrototypeScoped prototypeScoped) {
        String counter = applicationScoped.getCounter();
        String response = """
                {
                   "bean count status": "%s",
                }""".formatted(counter);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
