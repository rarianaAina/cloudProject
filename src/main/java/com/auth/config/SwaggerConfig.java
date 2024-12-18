package com.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
            .info(new Info()
                .title("Authentication API")
                .version("1.0.0")
                .description("API for user authentication with 2FA"));
    }
}