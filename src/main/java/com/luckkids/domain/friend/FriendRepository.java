package com.luckkids.domain.friend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    void deleteAllByReceiverId(int requestId);

    void deleteAllByRequesterId(int receiverId);

    Optional<Friend> findByRequesterIdAndReceiverId(int requestId, int receiverId);
}
