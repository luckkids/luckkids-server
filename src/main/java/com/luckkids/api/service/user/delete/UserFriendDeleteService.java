package com.luckkids.api.service.user.delete;

import com.luckkids.domain.friend.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFriendDeleteService {
    private final FriendRepository friendRepository;

    public void deleteAllByUserId(int userId) {
        friendRepository.deleteAllByRequesterId(userId);
        friendRepository.deleteAllByReceiverId(userId);
    }
}
