/**
 * Represents a user entity in the authentication system.
 * This entity is mapped to the "users" table in the database.
 * It includes user details such as email, password, first name, last name, and status flags.
 * It also tracks the creation and update timestamps.
 * 
 * Relationships:
 * - OneToMany with VerificationToken: A user can have multiple verification tokens.
 * - OneToMany with TwoFactorCode: A user can have multiple two-factor authentication codes.
 * 
 * Annotations:
 * - @Entity: Specifies that this class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Id: Specifies the primary key of the entity.
 * - @GeneratedValue: Provides the specification of generation strategies for the primary key values.
 * - @Column: Used to specify the mapped column for a persistent property or field.
 * - @CreationTimestamp: Automatically populates the creation timestamp.
 * - @UpdateTimestamp: Automatically populates the update timestamp.
 * - @OneToMany: Defines a one-to-many relationship with another entity.
 * - @Data: A Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 * 
 * Fields:
 * - id: The unique identifier for the user.
 * - email: The email address of the user, must be unique and not null.
 * - password: The password of the user.
 * - firstName: The first name of the user.
 * - lastName: The last name of the user.
 * - emailVerified: Flag indicating if the user's email is verified.
 * - enabled: Flag indicating if the user is enabled.
 * - loginAttempts: The number of login attempts made by the user.
 * - createdAt: The timestamp when the user was created.
 * - updatedAt: The timestamp when the user was last updated.
 * - verificationTokens: The list of verification tokens associated with the user.
 * - twoFactorCodes: The list of two-factor authentication codes associated with the user.
 */
package com.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    private boolean emailVerified = false;
    private boolean enabled = false;
    private int loginAttempts = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relation OneToMany pour les tokens de vérification (associés à un
    // utilisateur)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationToken> verificationTokens;

    // Relation OneToMany pour les codes 2FA (associés à un utilisateur)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TwoFactorCode> twoFactorCodes;
}
