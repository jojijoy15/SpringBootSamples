package com.example.beanscopes.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    public static final Logger log = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    RequestScoped requestScoped;

    @GetMapping("/requestId")
    public ResponseEntity<String> getRequestId() {
        requestScoped.responseWithRequestId();
        String response = """
                {
                    "status" : "All good"
                }
                """;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
