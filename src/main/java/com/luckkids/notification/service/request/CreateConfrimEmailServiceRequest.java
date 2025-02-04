package com.luckkids.notification.service.request;

import com.luckkids.notification.domain.confirmEmail.ConfirmEmail;
import com.luckkids.notification.domain.confirmEmail.ConfirmStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateConfrimEmailServiceRequest {
	private String email;
	private String authKey;

	@Builder
	private CreateConfrimEmailServiceRequest(String email, String authKey) {
		this.email = email;
		this.authKey = authKey;
	}

	public ConfirmEmail toEntity() {
		return ConfirmEmail.builder()
			.email(email)
			.authKey(authKey)
			.confirmStatus(ConfirmStatus.INCOMPLETE)
			.build();
	}
}
