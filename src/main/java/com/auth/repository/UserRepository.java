/**
 * Repository interface for {@link User} entity.
 * Extends {@link JpaRepository} to provide CRUD operations.
 * 
 * <p>This repository provides methods to perform operations on the User entity, 
 * such as finding a user by email, checking if a user exists by email, 
 * and finding a user by email with the enabled status set to true.</p>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #findByEmail(String)}: Finds a user by their email.</li>
 *   <li>{@link #existsByEmail(String)}: Checks if a user exists by their email.</li>
 *   <li>{@link #findByEmailAndEnabledTrue(String)}: Finds a user by their email if they are enabled.</li>
 * </ul>
 * 
 * @see User
 * @see JpaRepository
 */
package com.auth.repository;

import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEnabledTrue(String email);

}