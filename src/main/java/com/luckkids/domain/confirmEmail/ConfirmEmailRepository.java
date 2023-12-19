package com.luckkids.domain.confirmEmail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmEmailRepository extends JpaRepository<ConfirmEmail, Integer> {
    Optional<ConfirmEmail> findByEmail(String email);
    Optional<ConfirmEmail> findByEmailAndAuthKey(String email, String authkey);
}
