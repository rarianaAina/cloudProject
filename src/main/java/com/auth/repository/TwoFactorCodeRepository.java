package com.auth.repository;

import com.auth.entity.TwoFactorCode;
import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findByUserAndCodeAndUsedFalse(User user, String code);

    @Query("SELECT t FROM TwoFactorCode t WHERE t.user = :user AND t.expiryDate > CURRENT_TIMESTAMP AND t.used = false")
    Optional<TwoFactorCode> findActiveCodeByUser(@Param("user") User user);
}
