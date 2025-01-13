package com.luckkids.api.service.mission;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.misson.AlertStatus.*;
import static com.luckkids.domain.misson.MissionActive.*;
import static com.luckkids.domain.misson.MissionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.mission.request.MissionCreateServiceRequest;
import com.luckkids.api.service.mission.request.MissionUpdateServiceRequest;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.SuccessChecked;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionActive;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.misson.MissionType;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class MissionServiceTest extends IntegrationTestSupport {

	@Autowired
	private MissionService missionService;

	@Autowired
	private MissionRepository missionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MissionOutcomeRepository missionOutcomeRepository;

	@AfterEach
	void tearDown() {
		missionOutcomeRepository.deleteAllInBatch();
		missionRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("미션 내용들을 받아 미션을 생성한다.")
	@Test
	void createMission() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, AlertStatus.CHECKED, LocalTime.of(0, 0));
		missionRepository.save(mission);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		MissionCreateServiceRequest request = MissionCreateServiceRequest.builder()
			.missionType(HEALTH)
			.missionDescription("책 읽기")
			.alertStatus(AlertStatus.CHECKED)
			.alertTime(LocalTime.of(23, 30))
			.build();

		// when
		MissionResponse missionResponse = missionService.createMission(request);

		// then
		assertThat(missionResponse)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.contains(HEALTH, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30));

		List<Mission> missions = missionRepository.findAll();
		assertThat(missions).hasSize(2)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "운동하기", AlertStatus.CHECKED, LocalTime.of(0, 0)),
				tuple(HEALTH, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30))
			);
	}

	@DisplayName("미션 생성중 user의 id가 없는 예외가 발생한다.")
	@Test
	void createMissionWithException() {
		// given
		int userId = 1;

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(userId));

		MissionCreateServiceRequest request = MissionCreateServiceRequest.builder()
			.missionType(SELF_DEVELOPMENT)
			.missionDescription("책 읽기")
			.alertStatus(AlertStatus.CHECKED)
			.alertTime(LocalTime.of(23, 30))
			.build();

		// when // then
		assertThatThrownBy(() -> missionService.createMission(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 유저는 없습니다. id = " + userId);
	}

	@DisplayName("미션을 생성할 때 미션결과 데이터도 생성된다.")
	@Test
	@Transactional
	void createMissionWithEventPublication() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, AlertStatus.CHECKED, LocalTime.of(0, 0));

		userRepository.save(user);
		missionRepository.save(mission);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		MissionCreateServiceRequest request = MissionCreateServiceRequest.builder()
			.missionType(SELF_DEVELOPMENT)
			.missionDescription("책 읽기")
			.alertStatus(AlertStatus.CHECKED)
			.alertTime(LocalTime.of(23, 30))
			.build();

		// when
		MissionResponse createdMission = missionService.createMission(request);

		// then
		List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();
		assertThat(missionOutcomes).hasSize(1)
			.extracting(
				missionOutcome -> missionOutcome.getMission().getId(),
				MissionOutcome::getMissionStatus
			)
			.containsExactly(tuple(createdMission.getId(), FAILED));
	}

	@DisplayName("수정할 미션 내용들을 받아 미션을 수정한다.")
	@Test
	void updateMission() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, AlertStatus.CHECKED, LocalTime.of(0, 0));
		Mission savedMission = missionRepository.save(mission);

		int missionId = savedMission.getId();

		MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
			.missionType(SELF_DEVELOPMENT)
			.missionDescription("책 읽기")
			.alertStatus(AlertStatus.CHECKED)
			.alertTime(LocalTime.of(23, 30))
			.build();

		// when
		MissionResponse missionResponse = missionService.updateMission(missionId, request);

		// then
		assertThat(missionResponse)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.contains(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30));

		List<Mission> missions = missionRepository.findAll();
		assertThat(missions).hasSize(1)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.containsExactlyInAnyOrder(
				tuple(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30))
			);
	}

	@DisplayName("수정할 미션 내용 하나만 받아 미션을 수정한다.")
	@Test
	void updateOneMission() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, UNCHECKED, LocalTime.of(0, 0));
		Mission savedMission = missionRepository.save(mission);

		int missionId = savedMission.getId();

		MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
			.missionDescription("책 읽기")
			.build();

		// when
		MissionResponse missionResponse = missionService.updateMission(missionId, request);

		// then
		assertThat(missionResponse)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.contains(HEALTH, "책 읽기", UNCHECKED, LocalTime.of(0, 0));

		List<Mission> missions = missionRepository.findAll();
		assertThat(missions).hasSize(1)
			.extracting("missionType", "missionDescription", "alertStatus", "alertTime")
			.containsExactlyInAnyOrder(
				tuple(HEALTH, "책 읽기", UNCHECKED, LocalTime.of(0, 0))
			);
	}

	/**
	 @DisplayName("수정할 미션 내용들을 받아 미션을 수정한다. (활성화 -> 비활성화 / missionOutcome 삭제 이벤트 발행)")
	 @Test void updateMission_deleteEvent() {
	 // given
	 User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
	 userRepository.save(user);

	 Mission mission = createMission(user, HEALTH, "운동하기", TRUE, AlertStatus.CHECKED, LocalTime.of(0, 0));
	 Mission savedMission = missionRepository.save(mission);

	 MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.now());
	 missionOutcomeRepository.save(missionOutcome);

	 int missionId = savedMission.getId();

	 MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
	 .missionType(SELF_DEVELOPMENT)
	 .missionDescription("책 읽기")
	 .missionActive(FALSE)
	 .alertStatus(AlertStatus.CHECKED)
	 .alertTime(LocalTime.of(23, 30))
	 .build();

	 // when
	 MissionResponse missionResponse = missionService.updateMission(missionId, request);

	 // then
	 assertThat(missionResponse)
	 .extracting("missionType", "missionDescription", "alertStatus", "alertTime")
	 .contains(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30));

	 List<Mission> missions = missionRepository.findAll();
	 assertThat(missions).hasSize(1)
	 .extracting("missionType", "missionDescription", "alertStatus", "alertTime")
	 .containsExactlyInAnyOrder(
	 tuple(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30))
	 );

	 List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();
	 assertThat(missionOutcomes).hasSize(0);
	 }

	 @DisplayName("수정할 미션 내용들을 받아 미션을 수정한다. (비활성화 -> 활성화 / missionOutcome 생성 이벤트 발행)")
	 @Test void updateMission_createEvent() {
	 // given
	 User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
	 userRepository.save(user);

	 Mission mission = createMission(user, HEALTH, "운동하기", FALSE, AlertStatus.CHECKED, LocalTime.of(0, 0));
	 Mission savedMission = missionRepository.save(mission);

	 int missionId = savedMission.getId();

	 MissionUpdateServiceRequest request = MissionUpdateServiceRequest.builder()
	 .missionType(SELF_DEVELOPMENT)
	 .missionDescription("책 읽기")
	 .missionActive(TRUE)
	 .alertStatus(AlertStatus.CHECKED)
	 .alertTime(LocalTime.of(23, 30))
	 .build();

	 // when
	 MissionResponse missionResponse = missionService.updateMission(missionId, request);

	 // then
	 assertThat(missionResponse)
	 .extracting("missionType", "missionDescription", "alertStatus", "alertTime")
	 .contains(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30));

	 List<Mission> missions = missionRepository.findAll();
	 assertThat(missions).hasSize(1)
	 .extracting("missionType", "missionDescription", "alertStatus", "alertTime")
	 .containsExactlyInAnyOrder(
	 tuple(SELF_DEVELOPMENT, "책 읽기", AlertStatus.CHECKED, LocalTime.of(23, 30))
	 );

	 List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();
	 assertThat(missionOutcomes).hasSize(1);
	 }
	 **/

	@DisplayName("미션 ID를 받아 미션을 삭제한다.(삭제일을 업데이트한다. Soft Delete)")
	@Test
	void deleteMission() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, UNCHECKED, LocalTime.of(0, 0));
		Mission savedMission = missionRepository.save(mission);

		int missionId = savedMission.getId();

		// when
		missionService.deleteMission(missionId, LocalDateTime.of(2023, 10, 31, 0, 0, 0));

		// then
		assertThat(missionRepository.findAllByDeletedDateIsNull()).isEmpty();

		assertThat(missionRepository.findAll()).hasSize(1)
			.extracting("missionDescription", "alertStatus", "alertTime", "deletedDate")
			.containsExactlyInAnyOrder(
				tuple("운동하기", UNCHECKED, LocalTime.of(0, 0), LocalDateTime.of(2023, 10, 31, 0, 0, 0))
			);
	}

	@DisplayName("미션 ID를 받아 미션을 삭제한다.(삭제일을 업데이트한다. Soft Delete) (missionOutcome 삭제 이벤트 발행)")
	@Test
	void deleteMission_deleteEvent() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO);
		userRepository.save(user);

		Mission mission = createMission(user, HEALTH, "운동하기", TRUE, UNCHECKED, LocalTime.of(0, 0));
		Mission savedMission = missionRepository.save(mission);

		MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.now());
		missionOutcomeRepository.save(missionOutcome);

		int missionId = savedMission.getId();

		// when
		missionService.deleteMission(missionId, LocalDateTime.of(2023, 10, 31, 0, 0, 0));

		// then
		assertThat(missionRepository.findAllByDeletedDateIsNull()).isEmpty();

		assertThat(missionRepository.findAll()).hasSize(1)
			.extracting("missionDescription", "alertStatus", "alertTime", "deletedDate")
			.containsExactlyInAnyOrder(
				tuple("운동하기", UNCHECKED, LocalTime.of(0, 0), LocalDateTime.of(2023, 10, 31, 0, 0, 0))
			);

		List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();
		assertThat(missionOutcomes).hasSize(0);
	}

	private User createUser(String email, String password, SnsType snsType) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.build();
	}

	private Mission createMission(User user, MissionType missionType, String missionDescription,
		MissionActive missionActive, AlertStatus alertStatus, LocalTime alertTime) {
		return Mission.builder()
			.user(user)
			.missionType(missionType)
			.missionDescription(missionDescription)
			.missionActive(missionActive)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}

	private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate) {
		return MissionOutcome.builder()
			.mission(mission)
			.missionDate(missionDate)
			.missionStatus(FAILED)
			.successChecked(SuccessChecked.UNCHECKED)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}
}