package com.example.di.order.controller;

import com.example.di.order.service.Notification;
import com.example.di.order.service.Sender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    /*
    Note:
        @Autowired
        Q. Can we use @Autowired on final field without constructor based injection?
        A. No
     */
    private final Sender sender;

    public NotificationController(Sender sender) {
        this.sender = sender;
    }

    @GetMapping("/greet/notifications")
    public ResponseEntity<ArrayNode> getAllNotifications() {
        List<Notification> notifications = sender.sendNotification();
        ArrayNode objectNode = new ObjectMapper().createArrayNode();
        notifications.forEach(notification -> objectNode.add(notification.toString()));
        return ResponseEntity.ok(objectNode);
    }

}
