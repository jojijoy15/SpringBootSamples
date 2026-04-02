package com.example.componentscan.prototype;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrototypeControllerCrossLifeCycle {
    /*
     Cross Life cycle
     */
    @Lookup
    private PrototypeScoped prototypeScoped () {
        return null;
    }

    @GetMapping("/prototype/cross/lifecycle")
    public ResponseEntity<String> getCount(PrototypeScoped prototypeScoped) {
        String counter = prototypeScoped.getCounter();
        String response = """
                {
                   "bean count status": "%s",
                }""".formatted(counter);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
