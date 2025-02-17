package com.luckkids.notification.service;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.notification.domain.confirmEmail.ConfirmEmail;
import com.luckkids.notification.infra.ConfirmEmailRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConfirmEmailReadService {
	private final ConfirmEmailRepository confirmEmailRepository;

	public ConfirmEmail findByEmail(String email) {
		return confirmEmailRepository.findByEmail(email)
			.orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_FAIL));
	}

	public ConfirmEmail findByEmailAndAuthKey(String email, String authKey) {
		return confirmEmailRepository.findByEmailAndAuthKey(email, authKey)
			.orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_FAIL));
	}
}
