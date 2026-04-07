package com.example.di.order.service;

import jakarta.annotation.Priority;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Priority(1)
@Order(2)
@Component
public class NotificationTwo implements Notification {
}
