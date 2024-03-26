package com.luckkids.domain.push;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.alertSetting.QAlertSetting;
import com.luckkids.domain.misson.AlertStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.luckkids.domain.alertSetting.QAlertSetting.alertSetting;
import static com.luckkids.domain.push.QPush.push;

@RequiredArgsConstructor
@Repository
public class PushQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Push> findAllByAlertType(AlertType alertType){
        return jpaQueryFactory
            .selectFrom(push)
            .join(push.alertSetting, alertSetting)
            .where(isAlertTypeEqualsTo(alertType))
            .fetch();
    }

    private BooleanExpression isAlertTypeEqualsTo(AlertType alertType){
        return switch (alertType) {
            case LUCK -> alertSetting.luckMessage.eq(AlertStatus.CHECKED);
            case MISSION -> alertSetting.mission.eq(AlertStatus.CHECKED);
            case NOTICE -> alertSetting.notice.eq(AlertStatus.CHECKED);
            default -> throw new LuckKidsException(ErrorCode.LUCKKIDS_CHARACTER_UNKNOWN);
        };
    }
}
