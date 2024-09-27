package com.luckkids.api.service.friend;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.friend.request.FriendStatusRequest;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class FriendReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private FriendReadService friendReadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@AfterEach
	void tearDown() {
		userCharacterRepository.deleteAllInBatch();
		luckkidsCharacterRepository.deleteAllInBatch();
		friendRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("사용자의 친구 리스트를 조회한다.")
	@Test
	void getFriendList() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 100);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 151);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 200);
		userRepository.saveAll(List.of(user1, user2, user3));

		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1);
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

		UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
		UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter2);
		UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
		userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user1, user3);
		Friend friend3 = createFriend(user2, user3);
		friendRepository.saveAll(List.of(friend1, friend2, friend3));

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		FriendListResponse response = friendReadService.getFriendList(request);

		// then
		MyProfileDto myProfile = response.getMyProfile();
		assertThat(myProfile)
			.extracting("myId", "nickname", "luckPhrase", "characterType", "level", "characterCount")
			.containsExactlyInAnyOrder(user1.getId(), "테스트1", "테스트1의 행운문구", CLOVER, 1, 1);

		PageCustom<FriendProfileDto> friendPagingList = response.getFriendList();

		List<FriendProfileDto> friendList = friendPagingList.getContent();
		assertThat(friendList)
			.extracting("friendId", "nickname", "luckPhrase", "characterType", "level", "characterCount")
			.containsExactlyInAnyOrder(
				tuple(user2.getId(), "테스트2", "테스트2의 행운문구", CLOVER, 2, 1),
				tuple(user3.getId(), "테스트3", "테스트3의 행운문구", CLOVER, 2, 2)
			);

		PageableCustom pageInfo = friendPagingList.getPageInfo();
		assertThat(pageInfo)
			.extracting("currentPage", "totalPage", "totalElement")
			.containsExactlyInAnyOrder(
				1, 1, 2L
			);
	}

	@DisplayName("사용자의 친구가 없을 시 빈리스트를 반환한다.")
	@Test
	void getFriendListWithoutFriend() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);
		userRepository.saveAll(List.of(user1, user2, user3));

		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1);
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2);
		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));

		UserCharacter userCharacter1 = createUserCharacter(user1, luckkidsCharacter1);
		UserCharacter userCharacter2 = createUserCharacter(user2, luckkidsCharacter2);
		UserCharacter userCharacter3 = createUserCharacter(user3, luckkidsCharacter2);
		userCharacterRepository.saveAll(List.of(userCharacter1, userCharacter2, userCharacter3));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user1, user3);
		friendRepository.saveAll(List.of(friend1, friend2));

		PageInfoServiceRequest request = PageInfoServiceRequest.builder()
			.page(1)
			.size(10)
			.build();

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user2.getId()));

		// when
		FriendListResponse response = friendReadService.getFriendList(request);

		// then
		MyProfileDto myProfile = response.getMyProfile();
		assertThat(myProfile)
			.extracting("myId", "nickname", "luckPhrase", "characterType", "level", "characterCount")
			.contains(user2.getId(), "테스트2", "테스트2의 행운문구", CLOVER, 2, 0);

		PageCustom<FriendProfileDto> friendPagingList = response.getFriendList();

		List<FriendProfileDto> friendList = friendPagingList.getContent();
		assertThat(friendList).hasSize(0).isEmpty();

		PageableCustom pageInfo = friendPagingList.getPageInfo();
		assertThat(pageInfo)
			.extracting("currentPage", "totalPage", "totalElement")
			.containsExactlyInAnyOrder(
				1, 0, 0L
			);
	}

	@DisplayName("친구요청자와 수신자가 이미 친구인지 체크한다.")
	@Test
	void checkFriendStatus() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);

		userRepository.saveAll(List.of(user1, user2, user3));

		Friend friend1 = createFriend(user1, user2);
		friendRepository.save(friend1);

		FriendStatusRequest friendStatusRequest = FriendStatusRequest.of(user1.getId(), user2.getId());
		boolean check = friendReadService.checkFriendStatus(friendStatusRequest);

		assertThat(check).isTrue();

		friendStatusRequest = FriendStatusRequest.of(user1.getId(), user3.getId());
		check = friendReadService.checkFriendStatus(friendStatusRequest);

		assertThat(check).isFalse();
	}

	@DisplayName("수신자 id을 받아 친구 id리스트를 조회한다.")
	@Test
	void getFriendIdList() {
		// given
		User user1 = createUser("test1@gmail.com", "test1234", "테스트1", "테스트1의 행운문구", 0);
		User user2 = createUser("test2@gmail.com", "test1234", "테스트2", "테스트2의 행운문구", 0);
		User user3 = createUser("test3@gmail.com", "test1234", "테스트3", "테스트3의 행운문구", 0);

		userRepository.saveAll(List.of(user1, user2, user3));

		Friend friend1 = createFriend(user1, user2);
		Friend friend2 = createFriend(user1, user3);
		Friend friend3 = createFriend(user3, user2);
		friendRepository.saveAll(List.of(friend1, friend2, friend3));

		// when
		Set<Integer> friendIds = friendReadService.getFriendIds(user1.getId());

		// then
		assertThat(friendIds).hasSize(2).isEqualTo(Set.of(user2.getId(), user3.getId()));
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

	private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.build();

	}

	private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter) {
		return UserCharacter.builder()
			.user(user)
			.luckkidsCharacter(luckkidsCharacter)
			.characterProgressStatus(IN_PROGRESS)
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