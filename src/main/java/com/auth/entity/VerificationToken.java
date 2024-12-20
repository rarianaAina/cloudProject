/**
 * Entity representing a verification token.
 * This token is used for verifying user actions such as email verification.
 * 
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@link Data} - Lombok annotation to generate getters, setters, and other utility methods.</li>
 *   <li>{@link Entity} - Specifies that the class is an entity and is mapped to a database table.</li>
 *   <li>{@link Table} - Specifies the table name in the database.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code id} - Unique identifier for the verification token, generated automatically.</li>
 *   <li>{@code token} - The verification token string.</li>
 *   <li>{@code expiryDate} - The date and time when the token expires.</li>
 *   <li>{@code user} - The user associated with this verification token. This is a many-to-one relationship.</li>
 * </ul>
 * 
 * <p>Relationships:</p>
 * <ul>
 *   <li>{@code user} - Many-to-one relationship with the {@link User} entity. The user_id column in the verification_tokens table references the id column in the users table.</li>
 * </ul>
 */
package com.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    // Relation ManyToOne vers l'utilisateur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
