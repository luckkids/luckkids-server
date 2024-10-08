package com.luckkids.api.service.push;

import com.luckkids.api.service.firebase.FirebaseService;
import com.luckkids.api.service.push.request.PushSoundChangeServiceRequest;
import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.api.service.push.request.SendPushServiceRequest;
import com.luckkids.api.service.push.response.PushSoundChangeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.push.Push;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PushService {

    private final PushReadService pushReadService;
    private final FirebaseService firebaseService;
    private final SecurityService securityService;

    public void sendPushAlertType(SendPushAlertTypeServiceRequest sendPushAlertTypeRequest){
        Set<String> pushTokenSet = new HashSet<>();
        for(Push push : pushReadService.findAllByAlertType(sendPushAlertTypeRequest.getAlertType())){
            String pushToken = null;
            if(!pushTokenSet.contains(push.getPushToken())) {
                pushToken = push.getPushToken();
            }
            firebaseService.sendPushNotification(sendPushAlertTypeRequest.toSendPushServiceRequest(push), pushToken);
            pushTokenSet.add(pushToken);
        }
    }

    public void sendPushToUser(SendPushServiceRequest sendPushServiceRequest){
        firebaseService.sendPushNotification(sendPushServiceRequest.toSendPushServiceRequest(), sendPushServiceRequest.getPush().getPushToken());
    }

    public PushSoundChangeResponse updateSound(PushSoundChangeServiceRequest pushChangeServiceRequest){
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        Push push = pushReadService.findByDeviceIdAndUser(pushChangeServiceRequest.getDeviceId(), userId);
        push.updateSound(pushChangeServiceRequest.getSound());
        return PushSoundChangeResponse.of(push);
    }
}
