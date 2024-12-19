/**
 * The AuthApplication class serves as the entry point for the Spring Boot application.
 * It contains the main method which is used to launch the application.
 * 
 * <p>This class is annotated with {@link SpringBootApplication}, which is a convenience 
 * annotation that adds all of the following:
 * <ul>
 *   <li>{@link org.springframework.boot.autoconfigure.EnableAutoConfiguration}</li>
 *   <li>{@link org.springframework.context.annotation.ComponentScan}</li>
 *   <li>{@link org.springframework.context.annotation.Configuration}</li>
 * </ul>
 * 
 * <p>When the application is started, the {@link SpringApplication#run(Class, String...)} 
 * method is called to bootstrap the application, starting the Spring context and the 
 * embedded server.
 * 
 * @see SpringApplication
 */
package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {
    /**
     * The main entry point of the application.
     * 
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}