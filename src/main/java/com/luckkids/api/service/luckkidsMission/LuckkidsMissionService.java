package com.luckkids.api.service.luckkidsMission;

import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionServiceRequest;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionSaveResponse;
import com.luckkids.api.service.push.PushService;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.domain.luckkidsMission.LuckkidsMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LuckkidsMissionService {

    private final LuckkidsMissionRepository luckkidsMissionRepository;
    private final PushService pushService;

    public LuckkidsMissionSaveResponse save(LuckkidsMissionServiceRequest luckkidsMissionRequest){
        LuckkidsMission luckkidsMission = luckkidsMissionRepository.save(luckkidsMissionRequest.toEntity());
        pushService.sendPushAlertType(luckkidsMissionRequest.toSendPushAlertTypeRequest(AlertType.MISSION));
        return LuckkidsMissionSaveResponse.of(luckkidsMission);
    }
}
