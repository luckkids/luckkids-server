package com.luckkids.api.service.alertHistory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.alertHistory.AlertHistory;
import com.luckkids.domain.alertHistory.AlertHistoryQueryRepository;
import com.luckkids.domain.alertHistory.AlertHistoryRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AlertHistoryReadService {

	private final AlertHistoryRepository alertHistoryRepository;
	private final AlertHistoryQueryRepository alertHistoryQueryRepository;
	private final SecurityService securityService;

	public AlertHistory findByOne(Long id) {
		return alertHistoryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 알림 내역은 없습니다. id = " + id));
	}

	public PageCustom<AlertHistoryResponse> getAlertHistory(PageInfoServiceRequest request) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		Page<AlertHistory> alertHistoryPage = alertHistoryQueryRepository.findByDeviceIdAndUserId(
			userId,
			request.toPageable()
		);

		List<AlertHistoryResponse> alertHistoryResponseList = alertHistoryPage.getContent().stream()
			.map(AlertHistoryResponse::of)
			.toList();

		return PageCustom.<AlertHistoryResponse>builder()
			.content(alertHistoryResponseList)
			.pageInfo(PageableCustom.of(alertHistoryPage))
			.build();
	}
}
