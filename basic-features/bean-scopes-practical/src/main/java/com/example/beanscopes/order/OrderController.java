package com.example.beanscopes.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private Sender sender;

    @GetMapping("/order")
    public ResponseEntity<String> order() {
        List<Notification> notifications = sender.sendNotification();
        String response = """
                {
                   "notifications list": "%s",
                }""".formatted(notifications);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
