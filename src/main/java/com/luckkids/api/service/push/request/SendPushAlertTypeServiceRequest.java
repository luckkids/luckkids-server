package com.luckkids.api.service.push.request;

import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.firebase.request.SendPushServiceRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendPushAlertTypeServiceRequest {

    private AlertType alertType;
    private String screenName;
    private String body;

    @Builder
    private SendPushAlertTypeServiceRequest(AlertType alertType, String screenName, String body) {
        this.alertType = alertType;
        this.screenName = screenName;
        this.body = body;
    }

    public SendPushServiceRequest toSendPushServiceRequest(Push push){
        return SendPushServiceRequest.builder()
            .body(body)
            .push(push)
            .screenName(screenName)
            .build();
    }

    public AlertHistoryServiceRequest toAlertHistoryServiceRequest(Push push){
        return AlertHistoryServiceRequest.builder()
            .push(push)
            .alertDescription(body)
            .build();
    }

}
