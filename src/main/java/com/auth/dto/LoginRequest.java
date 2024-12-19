/**
 * Data Transfer Object (DTO) for login requests.
 * This class is used to encapsulate the data required for a user to log in.
 * It includes the user's email and password.
 * 
 * Annotations are used to enforce validation constraints:
 * - @NotBlank ensures that the field is not null and not empty.
 * - @Email ensures that the email field contains a valid email address.
 * 
 * Lombok's @Data annotation is used to automatically generate 
 * boilerplate code such as getters, setters, toString, equals, and hashCode methods.
 */
package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}