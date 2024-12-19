/**
 * Repository interface for {@link VerificationToken} entity.
 * Extends {@link JpaRepository} to provide CRUD operations.
 * 
 * @author 
 */
package com.auth.repository;

import com.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}