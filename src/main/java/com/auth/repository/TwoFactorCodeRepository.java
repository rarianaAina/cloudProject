/**
 * Repository interface for managing {@link TwoFactorCode} entities.
 * Provides methods to perform CRUD operations and custom queries
 * related to two-factor authentication codes.
 *
 * Methods:
 * - {@link #findByUserAndCodeAndUsedFalse(User, String)}: Finds a two-factor code by user and code that has not been used.
 * - {@link #findActiveCodeByUser(User)}: Retrieves the active two-factor code for the given user.
 *
 * Usage:
 * This repository is used to interact with the database for operations
 * related to two-factor authentication codes.
 *
 * Example:
 * <pre>
 * {@code
 * Optional<TwoFactorCode> code = twoFactorCodeRepository.findActiveCodeByUser(user);
 * }
 * </pre>
 *
 * @see TwoFactorCode
 * @see User
 */
package com.auth.repository;

import com.auth.entity.TwoFactorCode;
import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findByUserAndCodeAndUsedFalse(User user, String code);

    /**
     * Retrieves the active two-factor code for the given user. A code is considered
     * active if it has not expired and has not been used.
     *
     * @param user the user to find the active two-factor code for
     * @return the active two-factor code, or empty if none is found
     */
    @Query("SELECT t FROM TwoFactorCode t WHERE t.user = :user AND t.expiryDate > CURRENT_TIMESTAMP AND t.used = false")
    Optional<TwoFactorCode> findActiveCodeByUser(@Param("user") User user);
}
