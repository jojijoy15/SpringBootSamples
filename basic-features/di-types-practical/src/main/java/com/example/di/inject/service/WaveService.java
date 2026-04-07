package com.example.di.inject.service;

import com.example.di.dto.Greetings;
import jakarta.annotation.Priority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WaveService {

    public Greetings getGreetings(String name) {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        return switch(hour) {
            case 1,2,3,4,5,6,7,8,9,10,11 -> new Greetings("Good, Morning! ".concat(name), now);
            case 12, 13, 14, 15 -> new Greetings("Good, Afternoon! ".concat(name), now);
            default -> new Greetings("Good, Evening! ".concat(name), now);
        };
    }
}
