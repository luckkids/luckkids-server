package com.luckkids.domain.luckkidsMission;

import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionActive.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionActive;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

@Transactional
class LuckkidsMissionQueryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private LuckkidsMissionQueryRepository luckkidsMissionQueryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MissionRepository missionRepository;

	@Autowired
	private LuckkidsMissionRepository luckkidsMissionRepository;

	@AfterEach
	void tearDown() {
		luckkidsMissionRepository.deleteAllInBatch();
		missionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("유저가 선택하지 않은 나머지 대표미션들을 조회한다.")
	@Test
	void findLuckkidsMissionsWithoutUserMission() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		LuckkidsMission luckkidsMission1 = createLuckkidsMission(HEALTH, "운동하기", LocalTime.of(19, 0));
		LuckkidsMission luckkidsMission2 = createLuckkidsMission(SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0));
		luckkidsMissionRepository.saveAll(List.of(luckkidsMission1, luckkidsMission2));

		Mission mission = createMission(user, luckkidsMission1.getId(), HEALTH, "운동하기", TRUE, UNCHECKED,
			LocalTime.of(19, 0));
		missionRepository.save(mission);

		// when
		List<LuckkidsMission> result = luckkidsMissionQueryRepository.findLuckkidsMissionsWithoutUserMission(
			user.getId());

		// then
		assertThat(result).hasSize(1);
		assertThat(result).extracting("missionType", "missionDescription", "alertTime")
			.containsExactly(
				tuple(SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0))
			);
	}

	private User createUser(String email, String password, SnsType snsType) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.build();
	}

	private Mission createMission(User user, Integer luckkidsMissionId, MissionType missionType,
		String missionDescription, MissionActive missionActive, AlertStatus alertStatus, LocalTime alertTime) {
		return Mission.builder()
			.user(user)
			.luckkidsMissionId(luckkidsMissionId)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.missionActive(missionActive)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
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
}