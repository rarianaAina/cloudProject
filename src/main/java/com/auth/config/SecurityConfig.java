/**
 * Security configuration class for the application.
 * Configures security settings including CORS, CSRF, and request authorization.
 */
package com.auth.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configure the security filter chain for the application.
     * <p>
     * Disables CSRF protection and configures CORS settings.
     * Authorizes requests based on the following rules:
     * <ul>
     * <li>Permits all requests to the "/api/auth/**" endpoint.</li>
     * <li>Permits all requests to the "/swagger-ui/**" endpoint.</li>
     * <li>Permits all requests to the "/v3/api-docs/**" endpoint.</li>
     * <li>Requires the "ADMIN" role for requests to the "/api/users/**" endpoint.</li>
     * <li>Requires authentication for all other requests.</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }

    /**
     * Provides a CORS configuration source for the application.
     * <p>
     * Configures CORS settings to allow requests from any origin with
     * specific HTTP methods and headers.
     * <ul>
     * <li>Allowed origins: *</li>
     * <li>Allowed methods: GET, POST, PUT, PATCH, DELETE, OPTIONS</li>
     * <li>Allowed headers: authorization, content-type, x-auth-token</li>
     * </ul>
     * Registers the CORS configuration for all endpoints.
     *
     * @return a configured CorsConfigurationSource
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Returns a password encoder for encoding user passwords.
     * <p>
     * Provides a BCryptPasswordEncoder for encoding user passwords.
     * BCryptPasswordEncoder is a robust password encoder that uses a
     * salted hash to store passwords.
     * <p>
     * This bean is used by the application to encode and decode user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
