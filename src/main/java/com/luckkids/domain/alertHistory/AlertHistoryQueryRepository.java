package com.luckkids.domain.alertHistory;

import static com.luckkids.domain.alertHistory.QAlertHistory.*;
import static java.util.Optional.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AlertHistoryQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<AlertHistory> findByDeviceIdAndUserId(String deviceId, int userId, Pageable pageable) {
		List<AlertHistory> content = jpaQueryFactory
			.select(alertHistory)
			.from(alertHistory)
			.where(
				isDeviceIdEqualTo(deviceId),
				isUserIdEqualsTo(userId)
			)
			.orderBy(alertHistory.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = getTotalAlertHistoriesCount(deviceId, userId);

		return new PageImpl<>(content, pageable, total);
	}

	private long getTotalAlertHistoriesCount(String deviceId, int userId) {
		return ofNullable(
			jpaQueryFactory
				.select(alertHistory.count())
				.from(alertHistory)
				.where(
					isDeviceIdEqualTo(deviceId),
					isUserIdEqualsTo(userId)
				)
				.fetchOne()
		).orElse(0L);
	}

	private BooleanExpression isDeviceIdEqualTo(String deviceId) {
		return alertHistory.push.deviceId.eq(deviceId);
	}

	private BooleanExpression isUserIdEqualsTo(int userId) {
		return alertHistory.push.user.id.eq(userId);
	}
}
