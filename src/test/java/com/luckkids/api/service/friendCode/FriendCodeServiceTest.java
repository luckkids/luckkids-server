package com.luckkids.api.service.friendCode;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.notification.service.AlertHistoryReadService;
import com.luckkids.notification.service.response.AlertHistoryResponse;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.notification.infra.AlertHistoryRepository;
import com.luckkids.notification.domain.alertHistory.AlertHistoryStatus;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.FriendStatus;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.domain.user.*;
import com.luckkids.jwt.dto.LoginUserInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
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
	@Autowired
	private AlertHistoryRepository alertHistoryRepository;
	@Autowired
	private AlertHistoryReadService alertHistoryReadService;

	@AfterEach
	void tearDown() {
		alertHistoryRepository.deleteAllInBatch();
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
	void refuseFriendTest() {
		// given
		User user1 = userRepository.save(createUser(1));

		userRepository.saveAll(List.of(user1));

		FriendCode code = createFriendCode(user1);
		friendCodeRepository.save(code);

		friendCodeService.refuseFriend("ABCDEFGH");

		FriendCode friendCode = friendCodeReadService.findByCode("ABCDEFGH");
		assertThat(friendCode.getUseStatus()).isEqualTo(UseStatus.USED);
	}

	@DisplayName("FriendCode로 nickname과 상태값을 조회한다.")
	@Test
	void findNickNameByCode() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);

		userRepository.saveAll(List.of(user1, user2, user3));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user2, user1);
		friendRepository.saveAll(List.of(friend1, friend2));

		FriendCode code = createFriendCode(user2);
		friendCodeRepository.save(code);

		FriendCodeNickNameServiceRequest friendCodeNickNameServiceRequest = FriendCodeNickNameServiceRequest.builder()
			.code("ABCDEFGH")
			.build();

		FriendCodeNickNameResponse friendCodeNickNameResponse = friendCodeService.findNickNameByCode(
			friendCodeNickNameServiceRequest);

		assertThat(friendCodeNickNameResponse).extracting("nickName", "status")
			.contains("테스트2", FriendStatus.ALREADY);
	}

	@DisplayName("FriendCode로 nickname과 상태값을 조회한다 시 처음받는 요청일 경우 AlertHistory를 생성한다.")
	@Test
	void findNickNameByCodeCreateAlertHistory() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);

		userRepository.saveAll(List.of(user1, user2, user3));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user2, user1);
		friendRepository.saveAll(List.of(friend1, friend2));

		FriendCode code = createFriendCode(user2);
		friendCodeRepository.save(code);

		FriendCodeNickNameServiceRequest friendCodeNickNameServiceRequest = FriendCodeNickNameServiceRequest.builder()
			.code("ABCDEFGH")
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		friendCodeService.findNickNameByCode(friendCodeNickNameServiceRequest);
		friendCodeService.findNickNameByCode(friendCodeNickNameServiceRequest);

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		// when
		PageCustom<AlertHistoryResponse> result = alertHistoryReadService.getAlertHistory(request);

		// then
		assertThat(result.getContent()).hasSize(1).extracting("alertDescription", "alertHistoryStatus")
			.contains(
				tuple("테스트2님이 친구 초대를 보냈어요!", AlertHistoryStatus.UNCHECKED)
			);
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

	private User createUser(String email, String password, String nickname, String luckPhrase, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(SnsType.NORMAL)
			.nickname(nickname)
			.luckPhrase(luckPhrase)
			.role(Role.USER)
			.settingStatus(SettingStatus.COMPLETE)
			.missionCount(missionCount)
			.build();
	}

	private LoginUserInfo createUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

	private FriendCode createFriendCode(User user) {
		return FriendCode.builder()
			.user(user)
			.code("ABCDEFGH")
			.useStatus(UseStatus.UNUSED)
			.build();
	}

	private Friend createFriend(User requester, User receiver) {
		return Friend.builder()
			.requester(requester)
			.receiver(receiver)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

}
