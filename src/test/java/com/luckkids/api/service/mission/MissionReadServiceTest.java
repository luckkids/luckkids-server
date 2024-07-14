package com.luckkids.api.service.mission;

import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mission.response.MissionAggregateResponse;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class MissionReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private MissionReadService missionReadService;

	@Autowired
	private MissionRepository missionRepository;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		missionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("로그인 된 유저(유저1)의 미션들을 가져온다.")
	@Test
	void getMission1() {
		// given
		User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO);
		User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1_1 = createMission(user1, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2_1 = createMission(user2, SELF_DEVELOPMENT, "책읽기", CHECKED, LocalTime.of(13, 0));
		Mission mission2_2 = createMission(user2, SELF_DEVELOPMENT, "공부하기", UNCHECKED, LocalTime.of(23, 0));

		userRepository.saveAll(List.of(user1, user2));
		missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		MissionAggregateResponse result = missionReadService.getMission();

		// then
		assertThat(result.getUserMissions().get(HEALTH)).extracting("missionType", "missionDescription", "alertStatus",
				"alertTime")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0))
			);
	}

	@DisplayName("로그인 된 유저(유저2)의 미션들을 가져온다.")
	@Test
	void getMission2() {
		// given
		User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO);
		User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1_1 = createMission(user1, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2_1 = createMission(user2, SELF_DEVELOPMENT, "책읽기", CHECKED, LocalTime.of(13, 0));
		Mission mission2_2 = createMission(user2, SELF_DEVELOPMENT, "공부하기", UNCHECKED, LocalTime.of(23, 0));

		userRepository.saveAll(List.of(user1, user2));
		missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user2.getId()));

		// when
		MissionAggregateResponse result = missionReadService.getMission();

		// then
		assertThat(result.getUserMissions().get(SELF_DEVELOPMENT)).extracting("missionType", "missionDescription",
				"alertStatus", "alertTime")
			.containsExactlyInAnyOrder(
				tuple(SELF_DEVELOPMENT, "책읽기", CHECKED, LocalTime.of(13, 0)),
				tuple(SELF_DEVELOPMENT, "공부하기", UNCHECKED, LocalTime.of(23, 0))
			);

	}

	@DisplayName("mission의 id를 받아 조회한다.")
	@Test
	void findByOne() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));

		userRepository.save(user);
		Mission savedMission = missionRepository.save(mission);

		// when
		Mission result = missionReadService.findByOne(savedMission.getId());

		// then
		assertThat(result).extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.containsExactly(HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));

	}

	@DisplayName("mission의 id를 받아 조회할 때 id가 없는 예외가 발생한다.")
	@Test
	void findByOneWithException() {
		// given
		int missionId = 1;

		// when // then
		assertThatThrownBy(() -> missionReadService.findByOne(missionId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 미션은 없습니다. id = " + missionId);
	}

	private User createUser(String email, String password, SnsType snsType) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.build();
	}

	private Mission createMission(User user, MissionType missionType, String missionDescription,
		AlertStatus alertStatus, LocalTime alertTime) {
		return Mission.builder()
			.user(user)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}