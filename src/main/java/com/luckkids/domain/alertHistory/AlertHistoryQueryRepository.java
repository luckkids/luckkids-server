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

	public Page<AlertHistory> findByDeviceId(String deviceId, Pageable pageable) {
		List<AlertHistory> content = jpaQueryFactory
			.select(alertHistory)
			.from(alertHistory)
			.where(isDeviceIdEqualTo(deviceId))
			.orderBy(alertHistory.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = getTotalAlertHistoriesCount(deviceId);

		return new PageImpl<>(content, pageable, total);
	}

	private long getTotalAlertHistoriesCount(String deviceId) {
		return ofNullable(
			jpaQueryFactory
				.select(alertHistory.count())
				.from(alertHistory)
				.where(isDeviceIdEqualTo(deviceId))
				.fetchOne()
		).orElse(0L);
	}

	private BooleanExpression isDeviceIdEqualTo(String deviceId) {
		return alertHistory.push.deviceId.eq(deviceId);
	}
}
