package com.luckkids.domain.missionOutcome;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionActive.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDetailDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

@Transactional
class MissionOutcomeQueryRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private MissionOutcomeQueryRepository missionOutcomeQueryRepository;

	@Autowired
	private MissionOutcomeRepository missionOutcomeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MissionRepository missionRepository;

	@DisplayName("미션 성공 여부를 받아 미션 결과를 조회한다.")
	@Test
	void findMissionDetailsByStatus1() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

		// when
		List<MissionOutcomeDetailDto> missionOutcomeDetailList =
			missionOutcomeQueryRepository.findMissionDetailsByStatus(Optional.empty(), user.getId(),
				LocalDate.of(2023, 10, 25));

		// then
		assertThat(missionOutcomeDetailList).hasSize(2)
			.extracting("missionType", "missionDescription", "alertTime", "missionStatus")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기", LocalTime.of(19, 0), FAILED),
				tuple(SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0), SUCCEED)
			);
	}

	@DisplayName("미션 성공 여부를 받아 미션 결과를 조회한다.")
	@Test
	void findMissionDetailsByStatus2() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

		// when
		List<MissionOutcomeDetailDto> missionOutcomeDetailList =
			missionOutcomeQueryRepository.findMissionDetailsByStatus(Optional.of(SUCCEED), user.getId(),
				LocalDate.of(2023, 10, 25));

		// then
		assertThat(missionOutcomeDetailList).hasSize(1)
			.extracting("missionType", "missionDescription", "alertTime", "missionStatus")
			.contains(tuple(SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0), SUCCEED));
	}

	@DisplayName("검색할 범위 날짜를 받아 성공한 미션들이 있는지 조회한다.")
	@Test
	void findMissionOutcomeByDateRangeAndStatus() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 11, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 11, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 11, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 11, 26), FAILED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		// when
		List<MissionOutcomeCalendarDto> missionOutcomes =
			missionOutcomeQueryRepository.findMissionOutcomeByDateRangeAndStatus(
				user.getId(),
				LocalDate.of(2023, 11, 1),
				LocalDate.of(2023, 12, 31)
			);

		// then
		assertThat(missionOutcomes).hasSize(2)
			.extracting("missionDate", "hasSucceed")
			.containsExactlyInAnyOrder(
				tuple(LocalDate.of(2023, 11, 25), false),
				tuple(LocalDate.of(2023, 11, 26), true)
			);

	}

	@DisplayName("미션 날짜를 받아서 성공한 미션 리스트를 조회한다.")
	@Test
	void findSuccessfulMissionsByDate() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		// when
		List<MissionOutcomeCalendarDetailDto> result =
			missionOutcomeQueryRepository.findSuccessfulMissionsByDate(user.getId(), LocalDate.of(2023, 12, 26));

		// then
		assertThat(result).hasSize(2)
			.extracting("missionType", "missionDescription")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기"),
				tuple(SELF_DEVELOPMENT, "책읽기")
			);
	}

	@DisplayName("미션 날짜를 받아서 성공한 미션 리스트를 조회한다. (결과값: 빈 리스트)")
	@Test
	void findSuccessfulMissionsIsEmpty() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		// when
		List<MissionOutcomeCalendarDetailDto> result =
			missionOutcomeQueryRepository.findSuccessfulMissionsByDate(user.getId(), LocalDate.of(2023, 12, 25));

		// then
		assertThat(result).hasSize(0).isEmpty();
	}

	@DisplayName("성공한 미션들의 총 개수를 조회한다.")
	@Test
	void findMissionOutcomesCount() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		// when
		Long missionOutcomesCount = missionOutcomeQueryRepository.findMissionOutcomesCount(user.getId());

		// then
		assertThat(missionOutcomesCount).isEqualTo(2);
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
			.missionActive(TRUE)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}

	private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate, MissionStatus missionStatus) {
		return MissionOutcome.builder()
			.mission(mission)
			.missionDate(missionDate)
			.missionStatus(missionStatus)
			.build();
	}

}