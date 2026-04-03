package com.example.beanscopes.order;

import jakarta.annotation.Priority;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
//@Priority(2)
@Component
public class NotificationOne implements Notification {
}
