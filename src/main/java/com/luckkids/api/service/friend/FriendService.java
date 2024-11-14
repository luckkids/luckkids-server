package com.luckkids.api.service.friend;

import com.luckkids.api.service.friend.response.FriendDeleteResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friend.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;
    private final SecurityService securityService;

    public FriendDeleteResponse deleteFriend(int friendId) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        friendRepository.deleteByRequesterIdAndReceiverId(userId, friendId);
        friendRepository.deleteByRequesterIdAndReceiverId(friendId, userId);
        return FriendDeleteResponse.of(friendId);
    }

}
