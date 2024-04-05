package com.luckkids.domain.misson;

import static com.luckkids.domain.luckkidsMission.QLuckkidsMission.*;
import static com.luckkids.domain.misson.QMission.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.luckkids.domain.misson.projection.LuckkidsUserMissionDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MissionQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<LuckkidsUserMissionDto> findLuckkidsUserMissionsByUserId(int userId) {

		List<LuckkidsUserMissionDto> connectedMissions = jpaQueryFactory
			.select(Projections.constructor(LuckkidsUserMissionDto.class,
				luckkidsMission.id,
				mission.id,
				luckkidsMission.missionType,
				luckkidsMission.missionDescription,
				luckkidsMission.alertTime,
				mission.alertStatus,
				Expressions.asBoolean(true).as("isLuckkidsMission"),
				mission.id.isNotNull().as("isSelected")))
			.from(luckkidsMission)
			.leftJoin(mission).on(luckkidsMission.id.eq(mission.luckkidsMissionId)
				.and(mission.user.id.eq(userId))
				.and(mission.deletedDate.isNull()))
			.fetch();

		List<LuckkidsUserMissionDto> standaloneMissions = jpaQueryFactory
			.select(Projections.constructor(LuckkidsUserMissionDto.class,
				Expressions.nullExpression(Integer.class),
				mission.id,
				mission.missionType,
				mission.missionDescription,
				mission.alertTime,
				mission.alertStatus,
				Expressions.asBoolean(false).as("isLuckkidsMission"),
				Expressions.asBoolean(true).as("isSelected")))
			.from(mission)
			.where(mission.user.id.eq(userId),
				mission.luckkidsMissionId.isNull(),
				mission.deletedDate.isNull())
			.fetch();

		connectedMissions.addAll(standaloneMissions);
		return connectedMissions;
	}
}
