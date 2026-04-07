package com.example.di.constructor.service;

import com.example.di.dto.Greetings;
import jakarta.annotation.Priority;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
/* Highest Precedence even when @Priority is specified in other bean */
//@Primary
@Priority(1)
/* Replacement to @Primary annotation*/
public class HindiGreetingService implements GreetingService {

    public Greetings getGreetings(String name) {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        return switch(hour) {
            case 1,2,3,4,5,6,7,8,9,10,11 -> new Greetings("सुप्रभात! ".concat(name), now);
            case 12, 13, 14, 15 -> new Greetings("नमस्कार! ".concat(name), now);
            default -> new Greetings("शुभ संध्या! ".concat(name), now);
        };
    }
}
