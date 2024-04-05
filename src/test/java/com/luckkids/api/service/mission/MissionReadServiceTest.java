package com.luckkids.api.service.mission;

import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.misson.projection.LuckkidsUserMissionDto;
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

	@Autowired
	private LuckkidsMissionRepository luckkidsMissionRepository;

	@AfterEach
	void tearDown() {
		luckkidsMissionRepository.deleteAllInBatch();
		missionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("로그인 된 유저(유저1)의 미션들을 가져온다.")
	@Test
	void getMission1() {
		// given
		User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO);
		User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.saveAll(List.of(user1, user2));

		LuckkidsMission luckkidsMission1 = createLuckkidsMission(HEALTH, "30분 이상 운동하기", LocalTime.of(17, 0));
		LuckkidsMission luckkidsMission2 = createLuckkidsMission(HOUSEKEEPING, "1분간 호텔 침구처럼 이부자리 정리하기",
			LocalTime.of(18, 0));
		luckkidsMissionRepository.saveAll(List.of(luckkidsMission1, luckkidsMission2));

		Mission mission1_1 = createMission(user1, luckkidsMission1.getId(), HEALTH, "30분 이상 운동하기", UNCHECKED,
			LocalTime.of(17, 0));
		Mission mission2_1 = createMission(user2, luckkidsMission2.getId(), HOUSEKEEPING, "1분간 호텔 침구처럼 이부자리 정리하기",
			CHECKED, LocalTime.of(18, 0));
		Mission mission2_2 = createMission(user2, null, SELF_DEVELOPMENT, "공부하기", UNCHECKED, LocalTime.of(23, 0));
		missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user1.getId()));

		// when
		Map<MissionType, List<LuckkidsUserMissionDto>> missions = missionReadService.getMission();

		// then
		assertThat(missions.get(HEALTH)).extracting("luckkidsMissionId", "missionId", "missionType",
				"missionDescription", "alertTime", "alertStatus", "isLuckkidsMission", "isSelected")
			.containsExactlyInAnyOrder(
				tuple(mission1_1.getLuckkidsMissionId(), mission1_1.getId(), mission1_1.getMissionType(),
					mission1_1.getMissionDescription(), mission1_1.getAlertTime(), mission1_1.getAlertStatus(), true,
					true)
			);

		assertThat(missions.get(HOUSEKEEPING)).extracting("luckkidsMissionId", "missionId", "missionType",
				"missionDescription", "alertTime", "alertStatus", "isLuckkidsMission", "isSelected")
			.containsExactlyInAnyOrder(
				tuple(luckkidsMission2.getId(), null, luckkidsMission2.getMissionType(),
					luckkidsMission2.getMissionDescription(), luckkidsMission2.getAlertTime(), null, true, false)
			);
	}

	@DisplayName("로그인 된 유저(유저2)의 미션들을 가져온다.")
	@Test
	void getMission2() {
		// given
		User user1 = createUser("user1@daum.net", "user1234!", SnsType.KAKAO);
		User user2 = createUser("user2@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.saveAll(List.of(user1, user2));

		LuckkidsMission luckkidsMission1 = createLuckkidsMission(HEALTH, "30분 이상 운동하기", LocalTime.of(17, 0));
		LuckkidsMission luckkidsMission2 = createLuckkidsMission(HOUSEKEEPING, "1분간 호텔 침구처럼 이부자리 정리하기",
			LocalTime.of(18, 0));
		luckkidsMissionRepository.saveAll(List.of(luckkidsMission1, luckkidsMission2));

		Mission mission1_1 = createMission(user1, luckkidsMission1.getId(), HEALTH, "30분 이상 운동하기", UNCHECKED,
			LocalTime.of(17, 0));
		Mission mission2_1 = createMission(user2, luckkidsMission2.getId(), HOUSEKEEPING, "1분간 호텔 침구처럼 이부자리 정리하기",
			CHECKED, LocalTime.of(18, 0));
		Mission mission2_2 = createMission(user2, null, SELF_DEVELOPMENT, "공부하기", UNCHECKED, LocalTime.of(23, 0));
		missionRepository.saveAll(List.of(mission1_1, mission2_1, mission2_2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user2.getId()));

		// when
		Map<MissionType, List<LuckkidsUserMissionDto>> missions = missionReadService.getMission();

		// then
		assertThat(missions.get(HOUSEKEEPING)).extracting("luckkidsMissionId", "missionId", "missionType",
				"missionDescription", "alertTime", "alertStatus", "isLuckkidsMission", "isSelected")
			.containsExactlyInAnyOrder(
				tuple(mission2_1.getLuckkidsMissionId(), mission2_1.getId(), mission2_1.getMissionType(),
					mission2_1.getMissionDescription(), mission2_1.getAlertTime(), mission2_1.getAlertStatus(), true,
					true)
			);

		assertThat(missions.get(SELF_DEVELOPMENT)).extracting("luckkidsMissionId", "missionId", "missionType",
				"missionDescription", "alertTime", "alertStatus", "isLuckkidsMission", "isSelected")
			.containsExactlyInAnyOrder(
				tuple(mission2_2.getLuckkidsMissionId(), mission2_2.getId(), mission2_2.getMissionType(),
					mission2_2.getMissionDescription(), mission2_2.getAlertTime(), mission2_2.getAlertStatus(), false,
					true)
			);
	}

	@DisplayName("mission의 id를 받아 조회한다.")
	@Test
	void findByOne() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission = createMission(user, null, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));

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

	private LuckkidsMission createLuckkidsMission(MissionType missionType, String missionDescription,
		LocalTime alertTime) {
		return LuckkidsMission.builder()
			.missionType(missionType)
			.missionDescription(missionDescription)
			.alertTime(alertTime)
			.build();
	}

	private Mission createMission(User user, Integer luckkidsMissionId, MissionType missionType,
		String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
		return Mission.builder()
			.user(user)
			.luckkidsMissionId(luckkidsMissionId)
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