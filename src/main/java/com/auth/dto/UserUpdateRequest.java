/**
 * Data Transfer Object for updating user information.
 * This class is used to encapsulate the data required to update a user's details.
 * 
 * Fields:
 * - firstName: The first name of the user. This field is mandatory and cannot be blank.
 * - lastName: The last name of the user. This field is mandatory and cannot be blank.
 * - password: The new password for the user. This field is optional.
 * 
 * Annotations:
 * - @NotBlank: Ensures that the annotated field is not null or empty.
 * - @Data: A Lombok annotation to generate boilerplate code such as getters, setters, toString, etc.
 */
package com.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String password;
}