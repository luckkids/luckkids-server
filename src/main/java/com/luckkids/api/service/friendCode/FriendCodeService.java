package com.luckkids.api.service.friendCode;

import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendCodeService {

    private final SecurityService securityService;
    private final FriendCodeRepository friendCodeRepository;
    private final FriendRepository friendRepository;
    private final UserReadService userReadService;
    private final FriendCodeReadService friendCodeReadService;

    public FriendInviteCodeResponse inviteCode() {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        String code = "";
        do {
            code = generateCode();
        } while (friendCodeRepository.existsByCode(code));

        User user = userReadService.findByOne(userId);

        friendCodeRepository.save(
            FriendCode.builder()
                .user(user)
                .code(code)
                .useStatus(UseStatus.UNUSED)
                .build()
        );

        return FriendInviteCodeResponse.of(code);
    }

    public FriendCreateResponse create(FriendCreateServiceRequest friendCreateServiceRequest){
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        FriendCode friendCode = friendCodeReadService.findByCode(friendCreateServiceRequest.getCode());

        friendCode.checkUsed();
        friendCode.updateUseStatus();

        User receiveUser = friendCode.getUser();
        User requestUser = userReadService.findByOne(userId);

        friendRepository.save(createFriend(requestUser, receiveUser));
        friendRepository.save(createFriend(receiveUser, requestUser));

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

    private Friend createFriend(User requestUser, User receiveUser){
        return Friend.builder()
            .requester(requestUser)
            .receiver(receiveUser)
            .build();
    }
}
