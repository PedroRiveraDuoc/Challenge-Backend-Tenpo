package com.example.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation.
 * Sets up Swagger UI with API information and grouping.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI challengeApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Challenge API")
                        .description("REST API for dynamic percentage calculations")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }
} 