package com.example.di.order.service;

import jakarta.annotation.Priority;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Priority(1) // What if negative?
//@Order(3)
@Component
public class NotificationThree implements Notification {
}
