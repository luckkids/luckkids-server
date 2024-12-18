package com.luckkids.api.service.luckkidsMission;

import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionListServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionListSaveResponse;
import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.api.service.push.request.SendPushDataDto;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import com.luckkids.domain.push.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LuckkidsMissionService {

    private final LuckkidsMissionRepository luckkidsMissionRepository;
    private final PushService pushService;

    public LuckkidsMissionListSaveResponse createLuckkidsMission(LuckkidsMissionListServiceRequest luckkidsMissionListServiceRequest) {
        List<LuckkidsMission> savedMissions = new ArrayList<>();

        luckkidsMissionListServiceRequest.getMissions().forEach(luckkidsMissionServiceRequest -> savedMissions.add(luckkidsMissionRepository.save(luckkidsMissionServiceRequest.toEntity())));

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
