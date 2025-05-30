package com.luckkids.api.service.missionOutcome;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.missionOutcome.SuccessChecked.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.Mission;
import com.luckkids.domain.misson.MissionRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.userCharacter.CharacterProgressStatus;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import com.luckkids.jwt.dto.LoginUserInfo;

class MissionOutcomeServiceTest extends IntegrationTestSupport {

	@Autowired
	private MissionOutcomeService missionOutcomeService;

	@Autowired
	private MissionOutcomeRepository missionOutcomeRepository;

	@Autowired
	private MissionRepository missionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@AfterEach
	void tearDown() {
		missionOutcomeRepository.deleteAllInBatch();
		missionRepository.deleteAllInBatch();
		userCharacterRepository.deleteAllInBatch();
		luckkidsCharacterRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("mission을 받아 미션결과 데이터를 생성한다.")
	@Test
	@Transactional
	void createMissionOutcome() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));

		userRepository.save(user);
		missionRepository.save(mission);

		MissionOutcomeCreateServiceRequest request = MissionOutcomeCreateServiceRequest.builder()
			.mission(mission)
			.missionDate(LocalDate.of(2023, 10, 25))
			.build();

		// when
		missionOutcomeService.createMissionOutcome(request);

		// then
		List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();

		assertThat(missionOutcomes).hasSize(1)
			.extracting("mission", "missionStatus", "missionDate")
			.containsExactly(tuple(mission, FAILED, LocalDate.of(2023, 10, 25)));
	}

	@DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트 한다.")
	@Test
	void updateMissionOutcome() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));

		userRepository.save(user);
		missionRepository.save(mission);
		MissionOutcome savedMissionOutcome = missionOutcomeRepository.save(missionOutcome);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		missionOutcomeService.updateMissionOutcome(savedMissionOutcome.getId(), SUCCEED);

		// then
		List<MissionOutcome> missionOutcomes = missionOutcomeRepository.findAll();

		assertThat(missionOutcomes).hasSize(1)
			.extracting("id", "missionStatus")
			.contains(
				tuple(savedMissionOutcome.getId(), SUCCEED)
			);
	}

	@DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트하고 결과를 확인한다. (레벨업 O)")
	@Test
	void updateMissionOutcomeStatusByIdAndVerifyResult_O() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 24);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));
		MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 27));
		MissionOutcome missionOutcome3 = createMissionOutcome(mission, LocalDate.of(2023, 10, 28));

		LuckkidsCharacter luckkidsCharacter1 = createLuckkidsCharacter(CLOVER, 1);
		LuckkidsCharacter luckkidsCharacter2 = createLuckkidsCharacter(CLOVER, 2);
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter1, IN_PROGRESS);

		userRepository.save(user);
		missionRepository.save(mission);
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3));

		luckkidsCharacterRepository.saveAll(List.of(luckkidsCharacter1, luckkidsCharacter2));
		userCharacterRepository.save(userCharacter);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		MissionOutcomeUpdateResponse response = missionOutcomeService.updateMissionOutcome(missionOutcome1.getId(),
			SUCCEED);

		// then
		assertThat(response)
			.extracting("levelUpResult", "characterType", "level")
			.contains(true, CLOVER, 2);
	}

	@DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트하고 결과를 확인한다. (레벨업 X)")
	@Test
	void updateMissionOutcomeStatusByIdAndVerifyResult_X() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome1 = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));
		MissionOutcome missionOutcome2 = createMissionOutcome(mission, LocalDate.of(2023, 10, 27));
		MissionOutcome missionOutcome3 = createMissionOutcome(mission, LocalDate.of(2023, 10, 28));

		LuckkidsCharacter luckkidsCharacter = createLuckkidsCharacter(CLOVER, 1);
		UserCharacter userCharacter = createUserCharacter(user, luckkidsCharacter, IN_PROGRESS);

		userRepository.save(user);
		missionRepository.save(mission);
		missionOutcomeRepository.saveAll(List.of(missionOutcome1, missionOutcome2, missionOutcome3));

		luckkidsCharacterRepository.save(luckkidsCharacter);
		userCharacterRepository.save(userCharacter);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		// when
		MissionOutcomeUpdateResponse response = missionOutcomeService.updateMissionOutcome(missionOutcome1.getId(),
			SUCCEED);

		// then
		assertThat(response)
			.extracting("levelUpResult", "characterType", "level")
			.contains(false, null, 0);
	}

	@DisplayName("동시성 테스트: 10개의 서로 다른 MissionOutcome 을 동시에 SUCCEED 업데이트 시 missionCount가 10이 되어야 한다.")
	@Test
	void updateMissionOutcome_concurrentUpdateTenOutcomes() throws InterruptedException {
		// given
		User user = createUser("user@daum.net", "passwd!", SnsType.KAKAO, 0);
		userRepository.save(user);

		List<Mission> missions = IntStream.rangeClosed(1, 10)
			.mapToObj(i -> createMission(user, "운동하기 " + i, AlertStatus.UNCHECKED, LocalTime.of(19, 0)))
			.toList();
		missionRepository.saveAll(missions);

		List<MissionOutcome> outcomes = missions.stream()
			.map(m -> createMissionOutcome(m, LocalDate.of(2023, 10, 26)))
			.toList();
		missionOutcomeRepository.saveAll(outcomes);

		given(securityService.getCurrentLoginUserInfo())
			.willReturn(createLoginUserInfo(user.getId()));

		int threadCount = outcomes.size();
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch readyLatch = new CountDownLatch(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);

		// when: 각 쓰레드가 준비 → 동시에 startLatch 오픈 → update 호출
		for (MissionOutcome mo : outcomes) {
			executor.submit(() -> {
				try {
					readyLatch.countDown();
					startLatch.await();
					missionOutcomeService.updateMissionOutcome(mo.getId(), SUCCEED);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		readyLatch.await();
		startLatch.countDown();

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.SECONDS);

		// then: User 의 missionCount 가 10이 되어야 한다
		User updated = userRepository.findById(user.getId())
			.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));
		assertThat(updated.getMissionCount()).isEqualTo(10);

		List<MissionOutcome> updatedOutcomes = missionOutcomeRepository.findAll();
		assertThat(updatedOutcomes)
			.hasSize(10)
			.extracting(MissionOutcome::getMissionStatus)
			.containsOnly(SUCCEED);
	}

	@DisplayName("업데이트할 id를 받아서 미션결과 상태를 업데이트 할 때 id가 없으면 예외가 발생한다.")
	@Test
	void updateMissionOutcomeWithExceptionId() {
		// given
		Long missionOutcomeId = 1L;

		// when // then
		assertThatThrownBy(() -> missionOutcomeService.updateMissionOutcome(missionOutcomeId, SUCCEED))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 미션결과는 없습니다. id = " + missionOutcomeId);
	}

	@DisplayName("업데이트할 missionStatus를 받아서 미션결과 상태를 업데이트 할 때 기존 상태와 같으면 예외가 발생한다.")
	@Test
	void updateMissionOutcomeWithExceptionMissionStatus() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));

		userRepository.save(user);
		missionRepository.save(mission);
		missionOutcomeRepository.save(missionOutcome);

		// when // then
		assertThatThrownBy(() -> missionOutcomeService.updateMissionOutcome(missionOutcome.getId(), FAILED))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("미션 상태가 기존과 같습니다.");
	}

	@DisplayName("삭제할 id를 받아서 미션결과를 삭제한다.")
	@Test
	void deleteMissionOutcome() {
		// given
		User user = createUser("user@daum.net", "user1234!", SnsType.KAKAO, 0);
		Mission mission = createMission(user, "운동하기", AlertStatus.UNCHECKED, LocalTime.of(19, 0));
		MissionOutcome missionOutcome = createMissionOutcome(mission, LocalDate.of(2023, 10, 26));

		userRepository.save(user);
		missionRepository.save(mission);
		missionOutcomeRepository.save(missionOutcome);
		int missionId = mission.getId();

		// when
		missionOutcomeService.deleteMissionOutcome(missionId, LocalDate.of(2023, 10, 26));

		// then
		assertThat(missionOutcomeRepository.findAll()).isEmpty();

	}

	private User createUser(String email, String password, SnsType snsType, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.missionCount(missionCount)
			.build();
	}

	private Mission createMission(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
		return Mission.builder()
			.user(user)
			.missionDescription(missionDescription)
			.alertStatus(alertStatus)
			.alertTime(alertTime)
			.build();
	}

	private MissionOutcome createMissionOutcome(Mission mission, LocalDate missionDate) {
		return MissionOutcome.builder()
			.mission(mission)
			.missionDate(missionDate)
			.missionStatus(FAILED)
			.successChecked(UNCHECKED)
			.build();
	}

	private LoginUserInfo createLoginUserInfo(int userId) {
		return LoginUserInfo.builder()
			.userId(userId)
			.build();
	}

	private LuckkidsCharacter createLuckkidsCharacter(CharacterType characterType, int level) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.build();

	}

	private UserCharacter createUserCharacter(User user, LuckkidsCharacter luckkidsCharacter,
		CharacterProgressStatus characterProgressStatus) {
		return UserCharacter.builder()
			.user(user)
			.luckkidsCharacter(luckkidsCharacter)
			.characterProgressStatus(characterProgressStatus)
			.build();
	}
}