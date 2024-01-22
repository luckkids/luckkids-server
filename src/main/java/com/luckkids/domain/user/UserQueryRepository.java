package com.luckkids.domain.user;

import com.luckkids.domain.user.projection.MyProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                user.luckPhrases,
                userCharacter.fileName,
                user.missionCount
            ))
            .from(user)
            .join(user.userCharacter, userCharacter)
            .where(user.id.eq(userId))
            .fetchOne();
    }
}
