package com.luckkids.api.service.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import com.luckkids.notification.service.AlertHistoryService;
import com.luckkids.notification.service.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.firebase.request.SendFirebaseDataDto;
import com.luckkids.api.service.firebase.request.SendFirebaseServiceRequest;
import com.luckkids.notification.domain.push.PushMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FirebaseService {

    private final FirebaseMessaging firebaseMessaging;
    private final AlertHistoryService alertHistoryService;
    private final ObjectMapper objectMapper;

    public void sendPushNotification(SendFirebaseServiceRequest sendPushServiceRequest, String pushToken) {
        try {
            // PushToken이 있으면 푸시전송
            if (pushToken != null) {
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

                firebaseMessaging.send(message);
            }
        } catch (FirebaseMessagingException e){
            log.error("Token: "+ pushToken + "Error: " + e.getMessage());
        } finally {
            //히스토리는 무조건 쌓는걸로
            alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(sendPushServiceRequest));
        }

    }

    private Map<String, Object> ObjectToMap(SendFirebaseDataDto sendFirebaseDataDto){
        return objectMapper.convertValue(sendFirebaseDataDto, Map.class);
    }
}
