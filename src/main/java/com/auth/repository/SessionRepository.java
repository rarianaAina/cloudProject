/*
package com.auth.repository;

import com.auth.entity.Session;
import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUserAndToken(String email, String token);

    Optional<Session> findByUser(User user); // Méthode pour retrouver la session active d'un utilisateur
}

*/
