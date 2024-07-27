package com.luckkids.api.service.friendCode;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.request.FriendStatusRequest;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.FriendStatus;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendCodeReadService {
    private final FriendCodeRepository friendCodeRepository;
    private final SecurityService securityService;
    private final FriendReadService friendReadService;

    public FriendCode findByCode(String code) {
        return friendCodeRepository.findByCode(code)
                .orElseThrow(() -> new LuckKidsException(ErrorCode.FRIEND_CODE_UNKNOWN));
    }

    public FriendCodeNickNameResponse findNickNameByCode(FriendCodeNickNameServiceRequest friendCreateServiceRequest) {
        int receiverId = securityService.getCurrentLoginUserInfo().getUserId(); // 수신자 ID
        FriendCode friendCode = findByCode(friendCreateServiceRequest.getCode());
        int requesterId = friendCode.getUser().getId(); // 요청자 ID

        // 내가 보낸 초대인지 체크
        if (requesterId == receiverId) {
            return FriendCodeNickNameResponse.of(friendCode.getUser().getNickname(), FriendStatus.ME);
        }

        // 이미 친구인지 체크
        boolean isAlreadyFriend = friendReadService.checkFriendStatus(FriendStatusRequest.of(requesterId, receiverId));
        FriendStatus friendStatus = isAlreadyFriend ? FriendStatus.ALREADY : FriendStatus.FRIEND;

        return FriendCodeNickNameResponse.of(friendCode.getUser().getNickname(), friendStatus);
    }

}
