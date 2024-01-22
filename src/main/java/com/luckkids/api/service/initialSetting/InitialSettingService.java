package com.luckkids.api.service.initialSetting;

import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.luckkids.domain.user.SettingStatus.COMPLETE;

@Service
@RequiredArgsConstructor
@Transactional
public class InitialSettingService {

    private final MissionService missionService;
    private final UserCharacterService userCharacterService;
    private final AlertSettingService alertSettingService;
    private final UserReadService userReadService;
    private final SecurityService securityService;

    public InitialSettingResponse initialSetting(InitialSettingServiceRequest request) {
        List<InitialSettingMissionResponse> initialSettingMissionResponses = request.getMissions().stream()
            .map(initialSettingMissionServiceRequest ->
                missionService.createMission(initialSettingMissionServiceRequest.toMissionServiceRequest(request.getAlertSetting().getAlertStatus())).toInitialSettingResponse())
            .toList();

        InitialSettingCharacterResponse initialSettingCharacterResponse = userCharacterService.createUserCharacter(request.getCharacter().toCharacterServiceRequest()).toInitialSettingResponse();

        InitialSettingAlertResponse initialSettingAlertResponse = alertSettingService.createAlertSetting(request.getAlertSetting().toServiceRequest()).toInitialSettingResponse();

        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        user.updateSettingStatus(COMPLETE);

        return InitialSettingResponse.of(initialSettingAlertResponse, initialSettingCharacterResponse, initialSettingMissionResponses);
    }
}
