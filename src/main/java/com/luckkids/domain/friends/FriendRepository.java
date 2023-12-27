package com.luckkids.domain.friends;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendQueryRepository {

    void deleteAllByReceiverId(int requestId);
    void deleteAllByRequesterId(int receiverId);

}
