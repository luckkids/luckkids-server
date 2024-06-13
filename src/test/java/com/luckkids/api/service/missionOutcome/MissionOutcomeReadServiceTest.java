package com.luckkids.api.service.missionOutcome;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionType.*;
import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeCountResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDetailDto;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class MissionOutcomeReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private MissionOutcomeReadService missionOutcomeReadService;

	@Autowired
	private MissionOutcomeRepository missionOutcomeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MissionRepository missionRepository;

	@AfterEach
	void tearDown() {
		missionOutcomeRepository.deleteAllInBatch();
		missionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("mission의 id를 받아 미션결과를 조회한다.")
	@Test
	@Transactional
	void findByOne() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 25), FAILED);

		userRepository.save(user);
		missionRepository.save(mission);
		MissionOutcome savedMission = missionOutcomeRepository.save(missionOutcome);

		// when
		MissionOutcome result = missionOutcomeReadService.findByOne(savedMission.getId());

		// then
		assertThat(result).extracting("mission", "missionStatus", "missionDate")
			.containsExactly(mission, FAILED, LocalDate.of(2023, 10, 25));

	}

	@DisplayName("mission의 id를 받아 미션결과를 조회할 때 id가 없는 예외가 발생한다.")
	@Test
	void findByOneWithException() {
		// given
		Long missionOutcomeId = 1L;

		// when // then
		assertThatThrownBy(() -> missionOutcomeReadService.findByOne(missionOutcomeId))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 미션결과는 없습니다. id = " + missionOutcomeId);
	}

	@DisplayName("없어도 되는 미션 성공 여부를 받아 미션 결과를 조회하여 응답을 반환한다.")
	@Test
	void getMissionDetailListForStatus() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 10, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 10, 25), SUCCEED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		List<MissionOutcomeResponse> missionOutcomeResponses = missionOutcomeReadService.getMissionOutcomeDetailListForStatus(
			empty(), LocalDate.of(2023, 10, 25));

		// then
		assertThat(missionOutcomeResponses).hasSize(2)
			.extracting("missionType", "missionDescription", "alertTime", "missionStatus")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기", LocalTime.of(19, 0), FAILED),
				tuple(SELF_DEVELOPMENT, "책읽기", LocalTime.of(20, 0), SUCCEED)
			);
	}

	@DisplayName("오늘 날짜를 받아 그 전 달의 1일부터 다음달의 마지막날까지의 범위에 미션 성공 여부를 조회한다.")
	@Test
	void getMissionOutcomeForCalendar() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), FAILED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		MissionOutcomeForCalendarResponse missionOutcomeForCalendarResponses
			= missionOutcomeReadService.getMissionOutcomeForCalendar(
			LocalDate.of(2023, 12, 22),
			date -> date.withDayOfMonth(1).minusMonths(1),
			date -> date.withDayOfMonth(1).plusMonths(1).minusDays(1)
		);

		// then
		assertThat(missionOutcomeForCalendarResponses.getStartDate()).isEqualTo(LocalDate.of(2023, 11, 1));
		assertThat(missionOutcomeForCalendarResponses.getEndDate()).isEqualTo(LocalDate.of(2023, 12, 31));

		assertThat(missionOutcomeForCalendarResponses.getCalendar()).hasSize(2)
			.extracting("missionDate", "hasSucceed")
			.containsExactlyInAnyOrder(
				tuple(LocalDate.of(2023, 12, 25), false),
				tuple(LocalDate.of(2023, 12, 26), true)
			);
	}

	@DisplayName("조회하고 싶은 미션 날짜를 받아 그 날의 성공한 미션 내용을 조회한다.")
	@Test
	void getMissionOutcomeForCalendarDetail() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), FAILED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		List<MissionOutcomeCalendarDetailDto> result =
			missionOutcomeReadService.getMissionOutcomeForCalendarDetail(LocalDate.of(2023, 12, 26));

		// then
		assertThat(result).hasSize(1)
			.extracting("missionType", "missionDescription")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기")
			);
	}

	@DisplayName("로그인한 유저의 성공한 미션들의 총 개수를 조회한다.")
	@Test
	void getMissionOutcomesCount() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission1 = createMission(user, HEALTH, "운동하기", UNCHECKED, LocalTime.of(19, 0));
		Mission mission2 = createMission(user, SELF_DEVELOPMENT, "책읽기", UNCHECKED, LocalTime.of(20, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome2 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 25), FAILED);
		MissionOutcome missionOutcome3 = createMissionOutcome(mission1, LocalDate.of(2023, 12, 26), SUCCEED);
		MissionOutcome missionOutcome4 = createMissionOutcome(mission2, LocalDate.of(2023, 12, 26), FAILED);

		userRepository.save(user);
		missionRepository.saveAll(List.of(mission1, mission2));
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3, missionOutcome4));

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		MissionOutcomeCountResponse missionOutcomesCount = missionOutcomeReadService.getMissionOutcomesCount();

		// then
		assertThat(missionOutcomesCount.getCount()).isEqualTo(1);
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

	private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate, MissionStatus missionStatus) {
		return MissionOutcome.builder()
			.mission(mission)
			.missionDate(missionDate)
			.missionStatus(missionStatus)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}