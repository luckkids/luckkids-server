package com.luckkids.api.service.initialSetting;

import static com.luckkids.domain.user.SettingStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.initialSetting.request.InitialSettingServiceRequest;
import com.luckkids.api.service.initialSetting.response.InitialSettingAlertResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingCharacterResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingMissionResponse;
import com.luckkids.api.service.initialSetting.response.InitialSettingResponse;
import com.luckkids.mission.service.MissionService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.UserCharacterService;
import com.luckkids.domain.user.User;

import lombok.RequiredArgsConstructor;

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
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		User user = userReadService.findByOne(userId);

		user.checkSettingStatus();

		List<InitialSettingMissionResponse> initialSettingMissionResponses = request.getMissions().stream()
			.map(initialSettingMissionServiceRequest ->
				missionService.createMission(initialSettingMissionServiceRequest.toMissionServiceRequest(
					request.getAlertSetting().getAlertStatus())).toInitialSettingResponse())
			.toList();

		InitialSettingCharacterResponse initialSettingCharacterResponse = userCharacterService.createUserCharacter(
			request.getCharacter().toCharacterServiceRequest()).toInitialSettingResponse();

		InitialSettingAlertResponse initialSettingAlertResponse = alertSettingService.createAlertSetting(
			request.getAlertSetting().toServiceRequest()).toInitialSettingResponse();

		user.updateSettingStatus(COMPLETE);

		return InitialSettingResponse.of(initialSettingAlertResponse, initialSettingCharacterResponse,
			initialSettingMissionResponses);
	}
}
