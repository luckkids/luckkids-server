package com.luckkids.api.service.firebase;

import com.google.firebase.messaging.*;
import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.firebase.request.SendPushServiceRequest;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final FirebaseMessaging firebaseMessaging;
    private final AlertHistoryService alertHistoryService;

    public void sendPushNotification(SendPushServiceRequest sendPushServiceRequest) {
        Message message = Message.builder()
            .setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                    .putCustomData("screen", sendPushServiceRequest.getScreenName())
                    .setSound(sendPushServiceRequest.getSound())
                    .setAlert(ApsAlert.builder()
                        .setTitle(PushMessage.TITLE.getText())
                        .setBody(sendPushServiceRequest.getBody())
                        .build())
                    .build())
                .build())
            .setToken(sendPushServiceRequest.getPush().getPushToken())
            .build();

        try {
            firebaseMessaging.send(message);
            alertHistoryService.createAlertHistory(createAlertHistoryServiceRequest(sendPushServiceRequest.getPush(), sendPushServiceRequest.getBody()));
        } catch (FirebaseMessagingException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private AlertHistoryServiceRequest createAlertHistoryServiceRequest(Push push, String alertDescription){
        return AlertHistoryServiceRequest.builder()
            .push(push)
            .alertDescription(alertDescription)
            .build();
    }
}
