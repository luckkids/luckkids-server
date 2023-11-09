package com.luckkids.api.service.friend;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.projection.FriendProfileReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadService {

    private final FriendRepository friendRepository;

    public PageCustom<FriendListReadResponse> readListFriend(int userId, PageInfoServiceRequest page){
        return PageCustom.of(friendRepository.readListFriend(userId, page));
    }

    public FriendProfileReadResponse readProfile(int friendId){
        return Optional.ofNullable(friendRepository.readProfile(friendId).toServiceResponse())
            .orElseThrow(() -> new LuckKidsException(ErrorCode.FRIEND_UNKNOWN));
    }
}
