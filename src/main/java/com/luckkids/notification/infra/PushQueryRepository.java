package com.luckkids.notification.infra;

import static com.luckkids.notification.domain.alertSetting.QAlertSetting.*;
import static com.luckkids.notification.domain.push.QPush.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.mission.domain.misson.AlertStatus;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.notification.domain.push.Push;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PushQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<Push> findAllByAlertType(AlertType alertType) {
		return jpaQueryFactory
			.selectFrom(push)
			.join(push.alertSetting, alertSetting)
			.where(isAlertTypeEqualsTo(alertType))
			.fetch();
	}

	private BooleanExpression isAlertTypeEqualsTo(AlertType alertType) {
		return switch (alertType) {
			case LUCK -> alertSetting.luckMessage.eq(AlertStatus.CHECKED);
			case MISSION -> alertSetting.mission.eq(AlertStatus.CHECKED);
			case NOTICE -> alertSetting.notice.eq(AlertStatus.CHECKED);
			case FRIEND -> alertSetting.friend.eq(AlertStatus.CHECKED);
			default -> throw new LuckKidsException(ErrorCode.LUCKKIDS_CHARACTER_UNKNOWN);
		};
	}
}
