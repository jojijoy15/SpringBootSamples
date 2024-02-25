package com.client.app.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.client.app")
@ComponentScan("com.client.app")
public class ApplicationConfiguration {

}
