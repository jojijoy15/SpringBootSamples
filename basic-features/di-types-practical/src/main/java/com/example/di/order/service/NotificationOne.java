package com.example.di.order.service;

import jakarta.annotation.Priority;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Priority(2) // What if negative?
@Order(1)
@Component
public class NotificationOne implements Notification {
}
