package com.luckkids.api.service.friendCode.request;

import com.luckkids.api.service.alertHistory.request.AlertHistoryServiceRequest;
import com.luckkids.api.service.alertSetting.request.AlertSettingCreateServiceRequest;
import com.luckkids.api.service.friend.request.FriendStatusRequest;
import com.luckkids.domain.alertHistory.AlertDestinationType;
import com.luckkids.domain.push.PushMessage;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendCodeNickNameServiceRequest {
    private String code;

    @Builder
    private FriendCodeNickNameServiceRequest(String code) {
        this.code = code;
    }

    public AlertHistoryServiceRequest toAlertHistoryServiceRequest(User user, String code) {
        return AlertHistoryServiceRequest.builder()
                .user(user)
                .alertDescription(PushMessage.FRIEND_CODE.getText().replace("{nickName}", user.getNickname()))
                .alertDestinationType(AlertDestinationType.FRIEND_CODE)
                .alertDestinationInfo(code)
                .build();
    }

}