/**
 * SignupRequest is a Data Transfer Object (DTO) used for user registration.
 * It contains the necessary fields for a user to sign up, including email, password, first name, and last name.
 * 
 * <p>Annotations are used to enforce validation constraints:
 * <ul>
 *   <li>{@link jakarta.validation.constraints.Email} ensures the email field contains a valid email address.</li>
 *   <li>{@link jakarta.validation.constraints.NotBlank} ensures the fields are not null or empty.</li>
 * </ul>
 * 
 * <p>The {@link lombok.Data} annotation is used to generate boilerplate code such as getters, setters, toString, equals, and hashCode methods.
 * 
 * <p>Fields:
 * <ul>
 *   <li>{@code email} - The user's email address.</li>
 *   <li>{@code password} - The user's password.</li>
 *   <li>{@code firstName} - The user's first name.</li>
 *   <li>{@code lastName} - The user's last name.</li>
 * </ul>
 */
package com.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
