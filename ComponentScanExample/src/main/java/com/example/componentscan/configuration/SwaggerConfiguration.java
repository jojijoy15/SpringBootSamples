package com.example.componentscan.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi swaggerModel() {
        return GroupedOpenApi.builder()
                .packagesToScan("com.example.componentscan")
                .addOpenApiCustomizer(openApi -> openApi.getInfo().setTitle("componentscan project"))
                .group("ComponentScanSpringSample")
                .build();
    }

    @Bean
    public OpenAPI openAPICustomization() {
        return new OpenAPI()
                .addServersItem(new Server().description("componentscan swaggger example"));
    }

}
