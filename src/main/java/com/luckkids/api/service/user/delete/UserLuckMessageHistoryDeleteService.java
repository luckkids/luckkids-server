package com.luckkids.api.service.user.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.domain.luckMessageHistory.LuckMessageHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLuckMessageHistoryDeleteService {
	private final LuckMessageHistoryRepository luckMessageHistoryRepository;

	public void deleteAllByUserId(int userId) {
		luckMessageHistoryRepository.deleteAllByPushUserId(userId);
	}
}
