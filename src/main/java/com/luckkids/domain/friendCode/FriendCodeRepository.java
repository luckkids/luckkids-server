package com.luckkids.domain.friendCode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendCodeRepository extends JpaRepository<FriendCode, Long> {
	boolean existsByCode(String code);

	void deleteAllByUserId(int receiverId);

	Optional<FriendCode> findByCode(String code);
}
