package com.auth.repository;

import com.auth.entity.TwoFactorCode;
import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findByUserAndCodeAndUsedFalse(User user, String code);
}