package com.luckkids.domain.friends;

import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.domain.friends.projection.FriendListReadDto;
import com.luckkids.domain.friends.projection.FriendProfileReadDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.luckkids.domain.userPhrase.QUserPhrase.userPhrase;
import static com.luckkids.domain.clover.QClover.clover;
import static com.luckkids.domain.friends.QFriend.friend;
import static com.luckkids.domain.user.QUser.user;
import static com.luckkids.domain.userCharacter.QUserCharacter.userCharacter;

@RequiredArgsConstructor
public class FriendQueryRepositoryImpl implements FriendQueryRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<FriendListReadResponse> readListFriend(int userId, PageInfoServiceRequest page) {
        Pageable pageable = page.toPageable();

        List<FriendListReadDto> results = queryFactory
            .select(Projections.constructor(FriendListReadDto.class,
                friend.receiver.id,
                userCharacter.characterName,
                userCharacter.fileName,
                user.missionCount
            ))
            .from(friend)
            .join(friend.receiver, user)
            .join(user.userCharacter, userCharacter)
            .where(
                friend.friendStatus.eq(FriendStatus.ACCEPTED)
                    .and(friend.requester.id.eq(userId))
            )
            .orderBy(user.missionCount.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> totalQuery = queryFactory
            .select(
                friend.count()
            )
            .from(friend)
            .join(friend.receiver, user)
            .join(user.userCharacter, userCharacter)
            .where(
                friend.friendStatus.eq(FriendStatus.ACCEPTED)
                    .and(friend.requester.id.eq(userId))
            );

        return PageableExecutionUtils.getPage(
                results.stream()
                        .map(dto -> dto.toServiceResponse())
                        .collect(Collectors.toList())
               , pageable, totalQuery::fetchOne);
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
}
