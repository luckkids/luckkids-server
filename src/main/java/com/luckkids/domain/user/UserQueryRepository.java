package com.luckkids.domain.user;

import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.user.projection.UserLeagueDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.luckkids.domain.luckkidsCharacter.QLuckkidsCharacter.luckkidsCharacter;
import static com.luckkids.domain.user.QUser.user;
import static com.luckkids.domain.userCharacter.QUserCharacter.userCharacter;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MyProfileDto getMyProfile(int userId) {
        return jpaQueryFactory
            .select(Projections.constructor(MyProfileDto.class,
                user.id,
                user.nickname,
                user.luckPhrase,
                luckkidsCharacter.imageFile,
                user.missionCount
            ))
            .from(user)
            .join(user.userCharacter, userCharacter)
            .join(userCharacter.luckkidsCharacter, luckkidsCharacter)
            .where(user.id.eq(userId))
            .fetchOne();
    }

    public List<UserLeagueDto> getUserLeagueTop3() {
        return jpaQueryFactory
            .select(Projections.constructor(UserLeagueDto.class,
                user.nickname,
                luckkidsCharacter.imageFile,
                user.missionCount
            ))
            .from(user)
            .join(user.userCharacter, userCharacter)
            .join(userCharacter.luckkidsCharacter, luckkidsCharacter)
            .orderBy(user.missionCount.desc())
            .limit(3)
            .fetch();
    }
}
