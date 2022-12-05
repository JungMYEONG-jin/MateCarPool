package com.example.eunboard.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder().
                group("v1-definition").
                pathsToMatch("/auth/**", "/member/**", "/ticket/**") .build();
    }

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("Mate Carpool").description("Mate Carpool API")
                .version("v1.0.0"));
    }

}
