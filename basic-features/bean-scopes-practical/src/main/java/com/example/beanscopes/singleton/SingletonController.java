package com.example.beanscopes.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonController {

    public static final Logger log = LoggerFactory.getLogger(SingletonController.class);

    @Autowired
    private SingletonScoped singletonScoped;

    @GetMapping("/singleton")
    public ResponseEntity<String> getCounter() {
        String time = singletonScoped.doSomething();
        String response = """
                {
                   "bean count status": "%s",
                }""".formatted(time);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
