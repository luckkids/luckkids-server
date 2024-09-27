package com.luckkids.domain.friend;

import static com.luckkids.domain.friend.QFriend.*;
import static com.luckkids.domain.user.QUser.*;
import static com.luckkids.domain.userCharacter.CharacterProgressStatus.*;
import static com.luckkids.domain.userCharacter.QUserCharacter.*;
import static java.util.Optional.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class FriendQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Page<FriendProfileDto> getFriendList(int userId, Pageable pageable) {
		List<FriendProfileDto> content = queryFactory
			.select(Projections.constructor(FriendProfileDto.class,
				friend.receiver.id,
				friend.receiver.nickname,
				friend.receiver.luckPhrase,
				userCharacter.luckkidsCharacter.characterType,
				userCharacter.luckkidsCharacter.level,
				user.missionCount
			))
			.from(friend)
			.join(friend.receiver, user)
			.join(user.userCharacter, userCharacter)
			.where(
				isRequesterIdEqualTo(userId),
				userCharacter.characterProgressStatus.eq(IN_PROGRESS)
			)
			.orderBy(user.missionCount.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = getTotalFriendsCount(userId);

		return new PageImpl<>(content, pageable, total);
	}

	public Set<Integer> findFriendIds(int userId) {
		List<Integer> requesterIds = queryFactory
			.select(friend.receiver.id)
			.from(friend)
			.where(friend.requester.id.eq(userId))
			.fetch();

		List<Integer> receiverIds = queryFactory
			.select(friend.requester.id)
			.from(friend)
			.where(friend.receiver.id.eq(userId))
			.fetch();

		HashSet<Integer> friendIds = new HashSet<>(requesterIds);
		friendIds.addAll(receiverIds);

		return friendIds;
	}

	private long getTotalFriendsCount(int userId) {
		return ofNullable(
			queryFactory
				.select(friend.count())
				.from(friend)
				.where(
					isRequesterIdEqualTo(userId)
				)
				.fetchOne()
		).orElse(0L);
	}

	private BooleanExpression isRequesterIdEqualTo(int userId) {
		return friend.requester.id.eq(userId);
	}
}
