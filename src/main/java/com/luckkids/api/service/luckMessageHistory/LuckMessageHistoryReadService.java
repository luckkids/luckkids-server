package com.luckkids.api.service.luckMessageHistory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.luckMessageHistory.request.LuckMessageHistoryServiceRequest;
import com.luckkids.api.service.luckMessageHistory.response.LuckMessageHistoryResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.luckMessageHistory.LuckMessageHistory;
import com.luckkids.domain.luckMessageHistory.LuckMessageHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LuckMessageHistoryReadService {

	private final LuckMessageHistoryRepository luckMessageHistoryRepository;
	private final SecurityService securityService;

	public LuckMessageHistory findByDeviceIdAndUserId(String deviceId) {
		return luckMessageHistoryRepository.findByPushDeviceIdAndPushUserId(deviceId,
				securityService.getCurrentLoginUserInfo().getUserId())
			.orElseThrow(() -> new LuckKidsException("오늘의 한마디 이력이 없습니다."));
	}

	public LuckMessageHistoryResponse findOne(LuckMessageHistoryServiceRequest luckMessageHistoryServiceRequest) {
		return LuckMessageHistoryResponse.of(findByDeviceIdAndUserId(luckMessageHistoryServiceRequest.getDeviceId()));
	}
}
