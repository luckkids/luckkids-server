package com.luckkids.domain.user;

import static com.luckkids.domain.luckkidsCharacter.QLuckkidsCharacter.*;
import static com.luckkids.domain.user.QUser.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static com.luckkids.domain.userCharacter.QUserCharacter.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.luckkids.domain.user.projection.InProgressCharacterDto;
import com.luckkids.domain.user.projection.MyProfileDto;
import com.luckkids.domain.user.projection.UserLeagueDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public InProgressCharacterDto getInProgressCharacter(int userId) {
		return jpaQueryFactory
			.select(Projections.constructor(InProgressCharacterDto.class,
				luckkidsCharacter.characterType,
				luckkidsCharacter.level
			))
			.from(user)
			.join(user.userCharacter, userCharacter)
			.join(userCharacter.luckkidsCharacter, luckkidsCharacter)
			.where(user.id.eq(userId),
				userCharacter.characterProgressStatus.eq(IN_PROGRESS)
			)
			.fetchOne();
	}

	public MyProfileDto getMyProfile(int userId) {
		return jpaQueryFactory
			.select(Projections.constructor(MyProfileDto.class,
				user.id,
				user.nickname,
				user.luckPhrase,
				luckkidsCharacter.characterType,
				luckkidsCharacter.level,
				user.missionCount
			))
			.from(user)
			.join(user.userCharacter, userCharacter)
			.join(userCharacter.luckkidsCharacter, luckkidsCharacter)
			.where(user.id.eq(userId),
				userCharacter.characterProgressStatus.eq(IN_PROGRESS)
			)
			.fetchOne();
	}

	public List<UserLeagueDto> getUserLeagueTop3() {
		return jpaQueryFactory
			.select(Projections.constructor(UserLeagueDto.class,
				user.id,
				user.nickname,
				luckkidsCharacter.characterType,
				luckkidsCharacter.level,
				user.missionCount
			))
			.from(user)
			.join(user.userCharacter, userCharacter)
			.join(userCharacter.luckkidsCharacter, luckkidsCharacter)
			.where(userCharacter.characterProgressStatus.eq(IN_PROGRESS))
			.orderBy(user.missionCount.desc())
			.limit(3)
			.fetch();
	}
}
