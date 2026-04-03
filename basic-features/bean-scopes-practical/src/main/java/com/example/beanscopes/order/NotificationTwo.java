package com.example.beanscopes.order;

import jakarta.annotation.Priority;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
//@Priority(1)
@Component
public class NotificationTwo implements Notification {
}
