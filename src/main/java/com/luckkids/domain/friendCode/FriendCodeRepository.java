package com.luckkids.domain.friendCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendCodeRepository extends JpaRepository<FriendCode, Long> {
    boolean existsByCode(String code);
    void deleteAllByUserId(int receiverId);
    Optional<FriendCode> findByCode(String code);
}
