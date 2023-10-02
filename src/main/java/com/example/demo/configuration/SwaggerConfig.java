package com.example.demo.configuration;

import io.swagger.v3.oas.models.annotations.OpenAPI31;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPI31
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("pl.bezskrajny")
                .pathsToMatch("/**")
                .build();
    }

}
