package com.luckkids.notification.service;

import com.luckkids.notification.service.request.ConfirmEmailCheckServiceRequest;
import com.luckkids.notification.service.request.CreateConfrimEmailServiceRequest;
import com.luckkids.notification.service.response.ConfirmEmailCheckResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.notification.domain.confirmEmail.ConfirmEmail;
import com.luckkids.notification.infra.ConfirmEmailRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmEmailService {

	private final ConfirmEmailRepository confirmEmailRepository;
	private final ConfirmEmailReadService confirmEmailReadService;
	private final SecurityService securityService;

	public void confirmEmail(String encryptKey) {
		String decryptKey = securityService.decrypt(encryptKey);

		String[] value = decryptKey.split("/");

		String email = value[0];
		String authKey = value[1];

		confirmEmailReadService.findByEmailAndAuthKey(email, authKey).confirm();
	}

	public ConfirmEmailCheckResponse checkEmail(ConfirmEmailCheckServiceRequest confirmEmailCheckServiceRequest) {
		ConfirmEmail confirmEmail = confirmEmailReadService.findByEmailAndAuthKey(
			confirmEmailCheckServiceRequest.getEmail(), confirmEmailCheckServiceRequest.getAuthKey());
		confirmEmail.checkEmail();
		return ConfirmEmailCheckResponse.of(confirmEmail.getEmail());
	}

	public void createConfirmEmail(CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest) {
		confirmEmailRepository.save(createConfrimEmailServiceRequest.toEntity());
	}
}
