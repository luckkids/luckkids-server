package com.luckkids.api.service.friend;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.projection.FriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadService {

    private final FriendRepository friendRepository;
    private final SecurityService securityService;

    public PageCustom<FriendListReadResponse> readListFriend(PageInfoServiceRequest pageRequest) {
        int userId = securityService.getCurrentUserInfo().getUserId();
        Pageable pageable = pageRequest.toPageable();

        Page<FriendListDto> friendPage = friendRepository.getFriendsList(userId, pageable);

        Page<FriendListReadResponse> responsePage = friendPage.map(FriendListDto::toServiceResponse);

        return PageCustom.of(responsePage);
    }

    public FriendProfileReadResponse readProfile(int friendId) {
        return Optional.ofNullable(friendRepository.readProfile(friendId))
            .orElseThrow(() -> new LuckKidsException(ErrorCode.FRIEND_UNKNOWN)).toServiceResponse();
    }
}
