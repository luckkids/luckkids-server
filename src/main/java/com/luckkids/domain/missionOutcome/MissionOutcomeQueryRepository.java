package com.luckkids.domain.missionOutcome;

import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.luckkids.domain.missionOutcome.QMissionOutcome.missionOutcome;
import static com.luckkids.domain.misson.QMission.mission;

@RequiredArgsConstructor
@Repository
public class MissionOutcomeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MissionOutcomeDetailDto> findMissionDetailsByStatus(Optional<MissionStatus> missionStatus) {

        JPAQuery<MissionOutcomeDetailDto> query = jpaQueryFactory
            .select(Projections.constructor(MissionOutcomeDetailDto.class,
                missionOutcome.id,
                mission.missionDescription,
                mission.alertTime,
                missionOutcome.missionStatus))
            .from(missionOutcome)
            .join(missionOutcome.mission, mission);

        missionStatus.ifPresent(status -> query.where(missionOutcome.missionStatus.eq(status)));

        return query.fetch();
    }
}
