package com.luckkids.api.service.friendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class FriendCodeServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendCodeRepository friendCodeRepository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendCodeService friendCodeService;
    @Autowired
    private FriendCodeReadService friendCodeReadService;

    @AfterEach
    void tearDown() {
        friendCodeRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구코드를 생성한다.")
    @Test
    void inviteCodeTest() {
        //given
        List<User> users = new ArrayList<>();

        IntStream.rangeClosed(1, 2).forEach(i -> {
            User user = createUser(i);
            users.add(user);
        });

        userRepository.saveAll(users);

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(users.get(0).getId()));

        //when
        FriendInviteCodeResponse friendInviteCodeResponse = friendCodeService.inviteCode();

        //then
        assertThat(friendInviteCodeResponse.getCode().length()).isEqualTo(8);
    }

    @DisplayName("친구코드를 생성시 존재하지 않는 사용자일시 에러가 발생한다.")
    @Test
    void inviteCodeWithoutUserTest() {
        //given
        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(1));

        //when, then
        assertThatThrownBy(() -> friendCodeService.inviteCode())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 유저는 없습니다. id = 1");
    }

    @DisplayName("친구코드를 입력하여 친구를 등록한다.")
    @Test
    void useFriendCode() {
        //given
        User user1 = userRepository.save(createUser(1));
        User user2 = userRepository.save(createUser(2));
        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        FriendInviteCodeResponse friendInviteCodeResponse = friendCodeService.inviteCode();

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(user2.getId()));

        FriendCreateServiceRequest friendCreateServiceRequest = FriendCreateServiceRequest.builder()
            .code(friendInviteCodeResponse.getCode())
            .build();

        //when
        FriendCreateResponse friendCreateResponse = friendCodeService.create(friendCreateServiceRequest);

        //then
        assertThat(friendCreateResponse)
            .extracting("requester", "receiver")
            .contains("test1", "test2");
    }

    @DisplayName("친구코드를 입력하여 친구를 등록할 시 이미 사용한 친구코드 일 시 예외가 발생한다.")
    @Test
    @Transactional
    void useFriendCodAlreadyUsed() {
        //given
        User user1 = userRepository.save(createUser(1));
        User user2 = userRepository.save(createUser(2));
        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(user1.getId()));

        FriendInviteCodeResponse friendInviteCodeResponse = friendCodeService.inviteCode();

        FriendCode friendCode = friendCodeReadService.findByCode(friendInviteCodeResponse.getCode());
        friendCode.updateUseStatus();

        given(securityService.getCurrentLoginUserInfo())
            .willReturn(createUserInfo(user2.getId()));

        FriendCreateServiceRequest friendCreateServiceRequest = FriendCreateServiceRequest.builder()
            .code(friendInviteCodeResponse.getCode())
            .build();

        //when, then
        assertThatThrownBy(() -> friendCodeService.create(friendCreateServiceRequest))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("이미 사용된 친구코드입니다.");
    }

    @DisplayName("친구요청을 거절시 사용한 코드로 상태를 변경한다.")
    @Test
     void refuseFriendTest(){
         // given
         User user1 = userRepository.save(createUser(1));

         userRepository.saveAll(List.of(user1));

         FriendCode code = createFriendCode(user1);
         friendCodeRepository.save(code);

         friendCodeService.refuseFriend("ABCDEFGH");

         FriendCode friendCode = friendCodeReadService.findByCode("ABCDEFGH");
         assertThat(friendCode.getUseStatus()).isEqualTo(UseStatus.USED);
     }

    private User createUser(int i) {
        return User.builder()
            .email("test" + i)
            .password("1234")
            .missionCount(i)
            .nickname("럭키즈!!!")
            .luckPhrase("행운입니다.")
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .build();
    }

    private LoginUserInfo createUserInfo(int userId) {
        return LoginUserInfo.builder()
            .userId(userId)
            .build();
    }

    private FriendCode createFriendCode(User user){
        return FriendCode.builder()
                .user(user)
                .code("ABCDEFGH")
                .useStatus(UseStatus.UNUSED)
                .build();
    }
}
