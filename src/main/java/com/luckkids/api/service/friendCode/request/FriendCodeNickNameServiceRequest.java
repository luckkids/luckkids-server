package com.luckkids.api.service.friendCode.request;

import com.luckkids.notification.service.request.AlertHistoryServiceRequest;
import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.push.PushMessage;
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

	public AlertHistoryServiceRequest toAlertHistoryServiceRequest(User user, String friendNickName, String code) {
		return AlertHistoryServiceRequest.builder()
			.user(user)
			.alertDescription(PushMessage.FRIEND_CODE.getText().replace("{nickName}", friendNickName))
			.alertDestinationType(AlertDestinationType.FRIEND_CODE)
			.alertDestinationInfo(code)
			.build();
	}

}
