package com.luckkids.mission.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.luckkids.mission.service.request.LuckkidsMissionListServiceRequest;
import com.luckkids.mission.service.response.LuckkidsMissionListSaveResponse;
import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.api.service.push.request.SendPushDataDto;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.infra.LuckkidsMissionRepository;
import com.luckkids.domain.push.PushMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LuckkidsMissionService {

	private final LuckkidsMissionRepository luckkidsMissionRepository;
	private final PushService pushService;

	public LuckkidsMissionListSaveResponse createLuckkidsMission(
		LuckkidsMissionListServiceRequest luckkidsMissionListServiceRequest) {
		List<LuckkidsMission> savedMissions = new ArrayList<>();

		luckkidsMissionListServiceRequest.getMissions()
			.forEach(luckkidsMissionServiceRequest -> savedMissions.add(
				luckkidsMissionRepository.save(luckkidsMissionServiceRequest.toEntity())));

		sendPushMission();

		return LuckkidsMissionListSaveResponse.of(savedMissions);
	}

	private void sendPushMission() {
		pushService.sendPushAlertType(
			SendPushAlertTypeServiceRequest.builder()
				.sendPushDataDto(
					SendPushDataDto.builder()
						.alert_destination_type(AlertDestinationType.MISSION)
						.build()
				)
				.body(PushMessage.MISSION.getText())
				.alertType(AlertType.MISSION)
				.build()
		);
	}
}
