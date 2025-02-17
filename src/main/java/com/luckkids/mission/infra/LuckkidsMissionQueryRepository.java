package com.luckkids.mission.infra;

import static com.luckkids.mission.domain.luckkidsMission.QLuckkidsMission.*;
import static com.luckkids.mission.domain.misson.QMission.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.luckkids.mission.domain.luckkidsMission.LuckkidsMission;
import com.luckkids.mission.domain.misson.MissionType;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class LuckkidsMissionQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<LuckkidsMission> findLuckkidsMissionsWithoutUserMission(int userId) {
		return jpaQueryFactory
			.select(luckkidsMission)
			.from(luckkidsMission)
			.where(JPAExpressions
				.selectOne()
				.from(mission)
				.where(mission.luckkidsMissionId.eq(luckkidsMission.id),
					mission.user.id.eq(userId))
				.notExists()
			)
			.fetch();
	}

	public List<MissionType> findAllGroupByMissionType() {
		return jpaQueryFactory
			.select(luckkidsMission.missionType)
			.from(luckkidsMission)
			.groupBy(luckkidsMission.missionType)
			.orderBy(luckkidsMission.sort.asc())
			.fetch();
	}
}
