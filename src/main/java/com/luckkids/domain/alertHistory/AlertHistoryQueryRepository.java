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

	public Page<AlertHistory> findByUserId(int userId, Pageable pageable) {
		List<AlertHistory> content = jpaQueryFactory
			.select(alertHistory)
			.from(alertHistory)
			.where(
				isUserIdEqualsTo(userId)
			)
			.orderBy(alertHistory.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = getTotalAlertHistoriesCount(userId);

		return new PageImpl<>(content, pageable, total);
	}

	public boolean hasUncheckedAlerts(int userId) {
		long count = ofNullable(
			jpaQueryFactory
				.select(alertHistory.count())
				.from(alertHistory)
				.where(
					isUserIdEqualsTo(userId),
					isStatusEqualsTo(AlertHistoryStatus.UNCHECKED)
				)
				.fetchOne()
		).orElse(0L);
		return count > 0;
	}

	private long getTotalAlertHistoriesCount(int userId) {
		return ofNullable(
			jpaQueryFactory
				.select(alertHistory.count())
				.from(alertHistory)
				.where(
					isUserIdEqualsTo(userId)
				)
				.fetchOne()
		).orElse(0L);
	}

	public boolean hasFriendCode(int userId, String friendCode) {
		long count = ofNullable(
				jpaQueryFactory
						.select(alertHistory.count())
						.from(alertHistory)
						.where(
								isUserIdEqualsTo(userId),
								isAlertDestinationTypeEqualsTo(AlertDestinationType.FRIEND_CODE),
								isAlertDestinationInfoEqualsTo(friendCode)
						)
						.fetchOne()
		).orElse(0L);
		return count > 0;
	}

	private BooleanExpression isUserIdEqualsTo(int userId) {
		return alertHistory.user.id.eq(userId);
	}

	private BooleanExpression isStatusEqualsTo(AlertHistoryStatus status) {
		return alertHistory.alertHistoryStatus.eq(status);
	}

	private BooleanExpression isAlertDestinationTypeEqualsTo(AlertDestinationType type) {
		return alertHistory.alertDestinationType.eq(type);
	}

	private BooleanExpression isAlertDestinationInfoEqualsTo(String code) {
		return alertHistory.alertDestinationInfo.eq(code);
	}
}
