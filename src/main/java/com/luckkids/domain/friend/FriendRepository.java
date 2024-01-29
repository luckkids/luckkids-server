package com.luckkids.domain.friend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    void deleteAllByReceiverId(int requestId);

    void deleteAllByRequesterId(int receiverId);
}
