package com.luckkids.api.service.missionOutcome;

import static com.luckkids.domain.missionOutcome.MissionStatus.*;
import static com.luckkids.domain.missionOutcome.SuccessChecked.*;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.missionOutcome.request.MissionOutcomeCreateServiceRequest;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeUpdateResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.api.service.userCharacter.response.UserCharacterLevelUpResponse;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionOutcomeService {

	private final MissionOutcomeRepository missionOutcomeRepository;
	private final UserRepository userRepository;

	private final MissionOutcomeReadService missionOutcomeReadService;
	private final UserCharacterService userCharacterService;
	private final UserReadService userReadService;
	private final UserService userService;
	private final SecurityService securityService;

	public void createMissionOutcome(MissionOutcomeCreateServiceRequest request) {
		MissionOutcome missionOutcome = request.toEntity();
		missionOutcomeRepository.save(missionOutcome);
	}

	public MissionOutcomeUpdateResponse updateMissionOutcome(Long missionOutcomeId, MissionStatus missionStatus) {
		MissionOutcome missionOutcome = missionOutcomeReadService.findByOne(missionOutcomeId);
		validateMissionStatus(missionOutcome, missionStatus);

		missionOutcome.updateMissionStatus(missionStatus);

		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		userRepository.updateMissionCount(userId, missionStatus.getValue());

		User user = userReadService.findByOne(userId);

		return updateMissionStatusAndCheckLevelUp(missionOutcome, missionStatus, user);
	}

	public void deleteMissionOutcome(int missionId, LocalDate missionDate) {
		userService.minusMissionCount(missionId, missionDate);
		missionOutcomeRepository.deleteAllByMissionIdAndMissionDate(missionId, missionDate);
	}

	public void validateMissionStatus(MissionOutcome missionOutcome, MissionStatus missionStatus) {
		if (missionOutcome.getMissionStatus().equals(missionStatus)) {
			throw new IllegalArgumentException("미션 상태가 기존과 같습니다.");
		}
	}

	public MissionOutcomeUpdateResponse updateMissionStatusAndCheckLevelUp(MissionOutcome missionOutcome,
		MissionStatus missionStatus, User user) {
		if (missionStatus == SUCCEED && missionOutcome.getSuccessChecked() == UNCHECKED) {
			missionOutcome.updateFirstSuccessChecked();
			UserCharacterLevelUpResponse userCharacterLevelUpResponse = userCharacterService.determineLevelUp(user);
			return userCharacterLevelUpResponse.toMissionOutcomeUpdateResponse();
		}
		return MissionOutcomeUpdateResponse.of(false, null, 0);
	}
}
