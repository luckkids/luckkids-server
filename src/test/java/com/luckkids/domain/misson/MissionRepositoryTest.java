package com.luckkids.domain.misson;

import static com.luckkids.domain.misson.AlertStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

@Transactional
class MissionRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MissionRepository missionRepository;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("삭제가 안 된(삭제일이 없는) 유저의 아이디로 미션을 조회한다.")
	@Test
	void findAllByUserIdAndDeletedDateIsNull() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, "운동하기", CHECKED, LocalTime.of(19, 0), null);
		Mission mission2 = createMission(user, "책읽기", CHECKED, LocalTime.of(22, 0), null);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));

		// when
		List<Mission> missions = missionRepository.findAllByUserIdAndDeletedDateIsNullOrderByMissionActiveDescAlertTimeAsc(
			user.getId());

		// then
		assertThat(missions).hasSize(2)
			.extracting("missionDescription", "alertStatus", "alertTime")
			.containsExactlyInAnyOrder(
				tuple("운동하기", CHECKED, LocalTime.of(19, 0)),
				tuple("책읽기", CHECKED, LocalTime.of(22, 0))
			);
	}

	@DisplayName("삭제가 안 된(삭제일이 없는) 미션 ID로 미션을 조회한다.")
	@Test
	void findByIdAndDeletedDateIsNull() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission = createMission(user, "운동하기", CHECKED, LocalTime.of(19, 0), null);

		userRepository.save(user);
		missionRepository.save(mission);

		// when
		Optional<Mission> foundMission = missionRepository.findByIdAndDeletedDateIsNull(mission.getId());

		// then
		assertThat(foundMission)
			.isPresent()
			.hasValueSatisfying(m -> {
				assertThat(m.getMissionDescription()).isEqualTo("운동하기");
				assertThat(m.getAlertStatus()).isEqualTo(CHECKED);
				assertThat(m.getAlertTime()).isEqualTo(LocalTime.of(19, 0));
				assertThat(m.getDeletedDate()).isNull();
			});
	}

	@DisplayName("삭제가 안 된(삭제일이 null인) 미션들만 조회한다.")
	@Test
	void findAllByDeletedDateIsNull() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, "운동하기", CHECKED, LocalTime.of(19, 0),
			LocalDateTime.of(2023, 10, 31, 0, 0, 0));
		Mission mission2 = createMission(user, "책읽기", CHECKED, LocalTime.of(22, 0), null);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));

		// when
		List<Mission> missions = missionRepository.findAllByDeletedDateIsNull();

		// then
		assertThat(missions).hasSize(1)
			.extracting("missionDescription", "alertStatus", "alertTime", "deletedDate")
			.containsExactly(tuple("책읽기", CHECKED, LocalTime.of(22, 0), null));
	}

	@DisplayName("사용자의 미션을 전부 삭제한다.")
	@Test
	void deleteAllByUserId() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, "운동하기", CHECKED, LocalTime.of(19, 0),
			LocalDateTime.of(2023, 10, 31, 0, 0, 0));
		Mission mission2 = createMission(user, "책읽기", CHECKED, LocalTime.of(22, 0), null);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));

		// when
		missionRepository.deleteAllByUserId(user.getId());

		List<Mission> missions = missionRepository.findAll();

		// then
		assertThat(missions).hasSize(0);
	}

	private User createUser(String email, String password, SnsType snsType) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.build();
	}

	private Mission createMission(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime,
		LocalDateTime deletedDate) {
		return Mission.builder()
			.user(user)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.deletedDate(deletedDate)
			.build();
	}
}