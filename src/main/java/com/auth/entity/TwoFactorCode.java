/**
 * Entity representing a two-factor authentication code.
 * This code is associated with a user and has an expiry date.
 * It also tracks whether the code has been used.
 * 
 * Annotations:
 * - @Data: Lombok annotation to generate getters, setters, and other utility
 * methods.
 * - @Entity: Specifies that this class is an entity and is mapped to a database
 * table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Id: Specifies the primary key of an entity.
 * - @GeneratedValue: Provides the specification of generation strategies for
 * the primary key values.
 * - @Pattern: Ensures the code follows a specific pattern (6-digit PIN).
 * - @ManyToOne: Defines a many-to-one relationship between this entity and the
 * User entity.
 * - @JoinColumn: Specifies the foreign key column.
 * 
 * Fields:
 * - id: Unique identifier for the two-factor code.
 * - code: The 6-digit PIN code used for two-factor authentication.
 * - expiryDate: The date and time when the code expires.
 * - used: Indicates whether the code has been used.
 * - user: The user associated with this two-factor code.
 */
package com.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "two_factor_codes")
public class TwoFactorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\d{6}", message = "The code must be a 6-digit PIN")
    private String code;

    private LocalDateTime expiryDate;

    // Propriété utilisée pour vérifier si le code a été utilisé
    private boolean used = false; // Assurez-vous que cette propriété existe

    // Relation ManyToOne vers l'utilisateur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
