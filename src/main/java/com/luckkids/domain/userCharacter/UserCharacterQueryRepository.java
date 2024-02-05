package com.luckkids.domain.userCharacter;

import com.luckkids.domain.userCharacter.projection.UserInProgressCharacterDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.luckkids.domain.luckkidsCharacter.QLuckkidsCharacter.luckkidsCharacter;
import static com.luckkids.domain.user.QUser.user;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.IN_PROGRESS;
import static com.luckkids.domain.userCharacter.QUserCharacter.userCharacter;

@RequiredArgsConstructor
@Repository
public class UserCharacterQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserInProgressCharacterDto findInProgressCharacterByUserId(int userId) {
        return jpaQueryFactory
            .select(Projections.constructor(UserInProgressCharacterDto.class,
                userCharacter,
                luckkidsCharacter
            ))
            .from(userCharacter)
            .join(userCharacter.user, user)
            .join(userCharacter.luckkidsCharacter, luckkidsCharacter)
            .where(userCharacter.user.id.eq(userId)
                .and(userCharacter.characterProgressStatus.eq(IN_PROGRESS)))
            .fetchOne();
    }
}
