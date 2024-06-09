package com.luckkids.domain.missionOutcome;

import static com.luckkids.domain.missionOutcome.QMissionOutcome.*;
import static com.luckkids.domain.misson.QMission.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDetailDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MissionOutcomeQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<MissionOutcomeDetailDto> findMissionDetailsByStatus(Optional<MissionStatus> missionStatus, int userId,
		LocalDate missionDate) {

		JPAQuery<MissionOutcomeDetailDto> query = jpaQueryFactory
			.select(Projections.constructor(MissionOutcomeDetailDto.class,
				missionOutcome.id,
				mission.missionType,
				mission.missionDescription,
				mission.alertStatus,
				mission.alertTime,
				missionOutcome.missionStatus))
			.from(missionOutcome)
			.where(mission.user.id.eq(userId),
				missionOutcome.missionDate.eq(missionDate))
			.join(missionOutcome.mission, mission);

		missionStatus.ifPresent(status -> query.where(missionOutcome.missionStatus.eq(status)));

		return query.fetch();
	}

	public List<MissionOutcomeCalendarDto> findMissionOutcomeByDateRangeAndStatus(int userId, LocalDate startDate,
		LocalDate endDate) {

		NumberExpression<Integer> successCount = new CaseBuilder()
			.when(missionOutcome.missionStatus.eq(MissionStatus.SUCCEED))
			.then(1)
			.otherwise(0)
			.sum();

		BooleanExpression isMissionSuccessful = successCount.gt(0);

		return jpaQueryFactory
			.select(Projections.constructor(MissionOutcomeCalendarDto.class,
				missionOutcome.missionDate,
				isMissionSuccessful))
			.from(missionOutcome)
			.join(missionOutcome.mission, mission)
			.where(mission.user.id.eq(userId),
				missionOutcome.missionDate.between(startDate, endDate))
			.groupBy(missionOutcome.missionDate)
			.orderBy(missionOutcome.missionDate.asc())
			.fetch();
	}

	public List<MissionOutcomeCalendarDetailDto> findSuccessfulMissionsByDate(int userId, LocalDate missionDate) {

		return jpaQueryFactory
			.select(Projections.constructor(MissionOutcomeCalendarDetailDto.class,
				mission.missionType,
				mission.missionDescription))
			.from(missionOutcome)
			.where(mission.user.id.eq(userId),
				missionOutcome.missionDate.eq(missionDate),
				missionOutcome.missionStatus.eq(MissionStatus.SUCCEED))
			.join(missionOutcome.mission, mission)
			.fetch();
	}
}
