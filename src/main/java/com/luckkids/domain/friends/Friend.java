package com.luckkids.domain.friends;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    @Builder
    private Friend(User requester, User receiver, FriendStatus friendStatus) {
        this.requester = requester;
        this.receiver = receiver;
        this.friendStatus = friendStatus;
    }
}
