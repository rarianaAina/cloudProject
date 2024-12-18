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
    private boolean used = false;  // Assurez-vous que cette propriété existe

    // Relation ManyToOne vers l'utilisateur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

