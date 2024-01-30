package com.luckkids.domain.luckkidsCharacter;

import com.luckkids.domain.luckkidsCharacter.projection.LuckkidsCharacterDto;
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
public class LuckkidsCharacterQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LuckkidsCharacterDto findCharacterByLevel(int level, int userId) {
        QLuckkidsCharacter lcInner = new QLuckkidsCharacter("lcInner");

        return jpaQueryFactory
            .select(Projections.constructor(LuckkidsCharacterDto.class,
                userCharacter.id,
                luckkidsCharacter.characterType,
                luckkidsCharacter.lottieFile,
                luckkidsCharacter.imageFile))
            .from(luckkidsCharacter)
            .join(lcInner).on(luckkidsCharacter.characterType.eq(lcInner.characterType))
            .join(userCharacter).on(lcInner.imageFile.eq(userCharacter.imageFile))
            .join(userCharacter.user, user)
            .where(user.id.eq(userId))
            .where(userCharacter.characterProgressStatus.eq(IN_PROGRESS))
            .where(luckkidsCharacter.level.eq(level))
            .fetchOne();
    }
}
