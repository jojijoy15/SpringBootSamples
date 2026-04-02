package com.example.componentscan.prototype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrototypeController {

    @Autowired
    ApplicationContext applicationContext;


    @GetMapping("/prototype")
    public ResponseEntity<String> getCount() {
        String counter = applicationContext.getBean(PrototypeScoped.class).getCounter();
        String response = """
                {
                   "bean count status": "%s",
                }""".formatted(counter);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
