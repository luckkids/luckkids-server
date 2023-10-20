package com.luckkids.domain.push;

import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Push {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private String pushToken;

	private String deviceId;

	@Builder
	private Push(User user, String pushToken, String deviceId) {
		this.user = user;
		this.pushToken = pushToken;
		this.deviceId = deviceId;
	}

	public static Push of(User user, String pushToken, String deviceId){
		return Push.builder()
			.user(user)
			.pushToken(pushToken)
			.deviceId(deviceId)
			.build();
	}

	public void updatePushToken(String pushToken){
		this.pushToken = pushToken;
	}
}
