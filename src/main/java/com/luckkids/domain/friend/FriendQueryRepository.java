package com.luckkids.domain.friend;

import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.luckkids.domain.friend.QFriend.friend;
import static com.luckkids.domain.user.QUser.user;
import static com.luckkids.domain.userCharacter.QUserCharacter.userCharacter;
import static java.util.Optional.ofNullable;

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
                userCharacter.imageFile,
                user.missionCount
            ))
            .from(friend)
            .join(friend.receiver, user)
            .join(user.userCharacter, userCharacter)
            .where(
                isRequesterIdEqualTo(userId)
            )
            .orderBy(user.missionCount.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = getTotalFriendsCount(userId);

        return new PageImpl<>(content, pageable, total);
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
