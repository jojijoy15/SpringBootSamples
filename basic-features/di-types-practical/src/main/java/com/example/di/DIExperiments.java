package com.example.di;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DIExperiments {


	public static void main(String[] args) {
		new SpringApplicationBuilder(DIExperiments.class)
					.run(args);

	}

}
