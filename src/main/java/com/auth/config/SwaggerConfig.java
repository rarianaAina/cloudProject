/**
 * Configuration class for setting up Swagger/OpenAPI documentation.
 * This class defines a bean that generates the OpenAPI documentation for the
 * API.
 * 
 * <p>
 * The generated documentation includes information such as the title, version,
 * and description of the API.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * &#64;Configuration
 * public class SwaggerConfig {
 *     &#64;Bean
 *     public OpenAPI api() {
 *         return new OpenAPI()
 *                 .info(new Info()
 *                         .title("Authentication API")
 *                         .version("1.0.0")
 *                         .description("API for user authentication with 2FA"));
 *     }
 * }
 * </pre>
 * 
 * <p>
 * Dependencies:
 * </p>
 * <ul>
 * <li>springdoc-openapi-ui</li>
 * <li>springdoc-openapi-webmvc-core</li>
 * </ul>
 * 
 * <p>
 * Ensure these dependencies are included in your project to enable Swagger UI.
 * </p>
 */
package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    /**
     * Generates the OpenAPI documentation for the API.
     * 
     * @return OpenAPI object containing the API documentation.
     */
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Authentication API")
                        .version("1.0.0")
                        .description("API for user authentication with 2FA"));
    }
}