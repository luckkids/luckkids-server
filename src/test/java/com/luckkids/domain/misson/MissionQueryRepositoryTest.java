package com.luckkids.domain.misson;

import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.misson.projection.LuckkidsUserMissionDto;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

class MissionQueryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MissionQueryRepository missionQueryRepository;

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

	@DisplayName("사용자 ID를 기준으로 luckkids_mission과 mission 테이블에서 미션들을 조회하여,"
		+ " 대표 미션 여부(is_luckkids_mission)와 선택된 미션 여부(is_selected)를 포함한 결과를 가져온다.")
	@Test
	void findLuckkidsUserMissionsByUserId() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		LuckkidsMission luckkidsMission1 = createLuckkidsMission(HEALTH, "30분 이상 운동하기", LocalTime.of(17, 0));
		LuckkidsMission luckkidsMission2 = createLuckkidsMission(HOUSEKEEPING, "1분간 호텔 침구처럼 이부자리 정리하기",
			LocalTime.of(18, 0));
		luckkidsMissionRepository.saveAll(List.of(luckkidsMission1, luckkidsMission2));

		Mission mission1 = createMission(user, null, HEALTH, "운동하기", UNCHECKED, LocalTime.of(20, 0));
		Mission mission2 = createMission(user, null, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(21, 0));
		Mission mission3 = createMission(user, luckkidsMission1.getId(), HEALTH, "30분 이상 운동하기", UNCHECKED,
			LocalTime.of(17, 0));
		missionRepository.saveAll(List.of(mission1, mission2, mission3));

		// when
		List<LuckkidsUserMissionDto> result = missionQueryRepository.findLuckkidsUserMissionsByUserId(user.getId());

		// then
		assertThat(result).hasSize(4)
			.extracting("luckkidsMissionId", "missionId", "missionType", "missionDescription", "alertTime",
				"alertStatus", "isLuckkidsMission", "isSelected")
			.containsExactlyInAnyOrder(
				tuple(luckkidsMission1.getId(), mission3.getId(), luckkidsMission1.getMissionType(),
					luckkidsMission1.getMissionDescription(), luckkidsMission1.getAlertTime(),
					mission3.getAlertStatus(), true, true),
				tuple(luckkidsMission2.getId(), null, luckkidsMission2.getMissionType(),
					luckkidsMission2.getMissionDescription(), luckkidsMission2.getAlertTime(), null, true, false),
				tuple(mission1.getLuckkidsMissionId(), mission1.getId(), mission1.getMissionType(),
					mission1.getMissionDescription(), mission1.getAlertTime(), mission1.getAlertStatus(), false, true),
				tuple(mission2.getLuckkidsMissionId(), mission2.getId(), mission2.getMissionType(),
					mission2.getMissionDescription(), mission2.getAlertTime(), mission2.getAlertStatus(), false, true)
			);
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
}