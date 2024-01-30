package com.luckkids.api.service.missionOutcome;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.luckkids.domain.missionOutcome.MissionStatus.SUCCEED;
import static com.luckkids.domain.missionOutcome.SuccessChecked.UNCHECKED;

@Transactional
@RequiredArgsConstructor
@Service
public class MissionOutcomeService {

    private final MissionOutcomeRepository missionOutcomeRepository;

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
        User user = getCurrentUser();
        userService.updateMissionCount(missionStatus, user);

        return updateMissionStatusAndCheckLevelUp(missionOutcome, missionStatus, user);
    }

    public void deleteMissionOutcome(int missionId, LocalDate missionDate) {
        missionOutcomeRepository.deleteAllByMissionIdAndMissionDate(missionId, missionDate);
    }

    private void validateMissionStatus(MissionOutcome missionOutcome, MissionStatus missionStatus) {
        if (missionOutcome.getMissionStatus().equals(missionStatus)) {
            throw new IllegalArgumentException("미션 상태가 기존과 같습니다.");
        }
    }

    private User getCurrentUser() {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        return userReadService.findByOne(userId);
    }

    private MissionOutcomeUpdateResponse updateMissionStatusAndCheckLevelUp(MissionOutcome missionOutcome, MissionStatus missionStatus, User user) {
        if (missionStatus == SUCCEED && missionOutcome.getSuccessChecked() == UNCHECKED) {
            missionOutcome.updateSuccessChecked();
            UserCharacterLevelUpResponse userCharacterLevelUpResponse = userCharacterService.determineLevelUp(user);
            return userCharacterLevelUpResponse.toMissionOutcomeUpdateResponse();
        }
        return MissionOutcomeUpdateResponse.of(false, null, null);
    }
}
