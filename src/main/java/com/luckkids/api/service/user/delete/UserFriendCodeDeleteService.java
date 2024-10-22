package com.luckkids.api.service.user.delete;

import com.luckkids.domain.friendCode.FriendCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFriendCodeDeleteService {

    private final FriendCodeRepository friendCodeRepository;

    public void deleteAllByUserId(int userId) {
        friendCodeRepository.deleteAllByUserId(userId);
    }
}
