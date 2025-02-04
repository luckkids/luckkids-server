package com.luckkids.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.luckkids.notification.domain.confirmEmail.ConfirmEmail;

public interface ConfirmEmailRepository extends JpaRepository<ConfirmEmail, Integer> {
	Optional<ConfirmEmail> findByEmail(String email);

	Optional<ConfirmEmail> findByEmailAndAuthKey(String email, String authkey);

	void deleteAllByEmail(String email);
}
