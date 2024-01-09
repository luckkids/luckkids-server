package com.luckkids.api.service.friend;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friend.response.FriendCreateResponse;
import com.luckkids.api.service.friend.response.FriendInviteCodeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.FriendStatus;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final SecurityService securityService;
    private final FriendCodeRepository friendCodeRepository;
    private final FriendRepository friendRepository;
    private final UserReadService userReadService;

    public FriendInviteCodeResponse inviteCode() {
        int userId = securityService.getCurrentUserInfo().getUserId();
        String code = generateCode();
        while(true){
            if(friendCodeRepository.existsByCode(code)){
                code = generateCode();
            }
            else{
                User user = userReadService.findByOne(userId);
                friendCodeRepository.save(
                    FriendCode.builder()
                        .user(user)
                        .code(code)
                        .build()
                );
                break;
            }
        }
        return FriendInviteCodeResponse.of(code);
    }

    public FriendCreateResponse create(FriendCreateServiceRequest friendCreateServiceRequest){
        int userId = securityService.getCurrentUserInfo().getUserId();
        FriendCode friendCode = friendCodeRepository.findByCode(friendCreateServiceRequest.getCode())
            .orElseThrow(() -> new LuckKidsException(ErrorCode.FRIEND_CODE_UNKNOWN));

        User receiveUser = friendCode.getUser();
        User requestUser = userReadService.findByOne(userId);

        Friend receiveFriend = Friend.builder()
            .requester(requestUser)
            .receiver(receiveUser)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();

        Friend requestFriend = Friend.builder()
            .requester(receiveUser)
            .receiver(requestUser)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();

        friendRepository.save(requestFriend);
        friendRepository.save(receiveFriend);

        return FriendCreateResponse.of(requestUser, receiveUser);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return random.ints(8, 0, chars.length())
            .mapToObj(chars::charAt)
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
