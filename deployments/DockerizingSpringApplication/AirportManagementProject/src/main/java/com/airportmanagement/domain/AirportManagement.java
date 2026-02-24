package com.airportmanagement.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.airportmanagement.domain.repository"
})
public class AirportManagement {

	public static void main(String[] args) {
		SpringApplication.run(AirportManagement.class, args);
	}

}
