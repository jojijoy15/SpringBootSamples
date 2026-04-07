package com.example.di.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Greetings {

    private String greeting;
    private LocalDateTime dateTime;
}
