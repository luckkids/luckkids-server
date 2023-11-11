package com.luckkids.domain.friends;

import com.luckkids.domain.friends.projection.FriendListDto;
import com.luckkids.domain.friends.projection.FriendProfileReadDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.luckkids.domain.friends.QFriend.friend;
import static com.luckkids.domain.user.QUser.user;
import static com.luckkids.domain.userCharacter.QUserCharacter.userCharacter;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class FriendQueryRepositoryImpl implements FriendQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FriendListDto> getFriendsList(int userId, Pageable pageable) {
        List<FriendListDto> content = queryFactory
            .select(Projections.constructor(FriendListDto.class,
                friend.receiver.id,
                userCharacter.characterName,
                userCharacter.fileName,
                user.missionCount
            ))
            .from(friend)
            .join(friend.receiver, user)
            .leftJoin(user.userCharacter, userCharacter)
            .where(getFriendshipCondition(userId))
            .orderBy(user.missionCount.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = getTotalFriendsCount(userId);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public FriendProfileReadDto readProfile(int friendId) {
        return queryFactory
            .select(Projections.constructor(FriendProfileReadDto.class,
                user.luckPharase.as("phraseDescription"),
                userCharacter.fileName.as("fileUrl"),
                userCharacter.characterName.as("characterName"),
                userCharacter.level.as("level")
            ))
            .from(user)
            .join(user.userCharacter, userCharacter)
            .where(user.id.eq(friendId))
            .fetchOne();
    }

    private long getTotalFriendsCount(int userId) {
        return ofNullable(
            queryFactory
                .select(friend.count())
                .from(friend)
                .where(getFriendshipCondition(userId))
                .fetchOne()
        ).orElse(0L);
    }

    private BooleanExpression getFriendshipCondition(int userId) {
        return friend.friendStatus.eq(FriendStatus.ACCEPTED)
            .and(friend.requester.id.eq(userId));
    }
}
