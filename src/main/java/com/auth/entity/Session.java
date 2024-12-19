/*
package com.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(name = "expire_le", nullable = false)
    private LocalDateTime expireLe;

    @CreationTimestamp
    @Column(name = "cree_le", nullable = false)
    private LocalDateTime creeLe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private User user;

}

*/
