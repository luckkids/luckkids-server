package com.luckkids.api.service.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.firebase.request.SendFirebaseDataDto;
import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.domain.push.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FirebaseService {

    private final FirebaseMessaging firebaseMessaging;
    private final AlertHistoryService alertHistoryService;
    private final ObjectMapper objectMapper;

    public void sendPushNotification(SendFirebaseServiceRequest sendPushServiceRequest) {
        String pushToken = sendPushServiceRequest.getPush().getPushToken();
        // PushToken이 없으면 메소드를 종료
        if (pushToken == null || pushToken.isEmpty()) return;

        Message message = Message.builder()
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .putAllCustomData(ObjectToMap(sendPushServiceRequest.getSendFirebaseDataDto()))
                                .setSound(sendPushServiceRequest.getSound())
                                .setAlert(ApsAlert.builder()
                                        .setTitle(PushMessage.TITLE.getText())
                                        .setBody(sendPushServiceRequest.getBody())
                                        .build())
                                .build())
                        .build())
                .setToken(pushToken)
                .build();

        try {
            firebaseMessaging.send(message);
            alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(sendPushServiceRequest));
        } catch (FirebaseMessagingException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Map<String, Object> ObjectToMap(SendFirebaseDataDto sendFirebaseDataDto){
        return objectMapper.convertValue(sendFirebaseDataDto, Map.class);
    }
}
