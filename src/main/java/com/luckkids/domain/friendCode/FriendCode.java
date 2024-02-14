package com.luckkids.domain.friendCode;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
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
public class FriendCode extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String code;

    @Enumerated(EnumType.STRING)
    private UseStatus useStatus;

    @Builder
    private FriendCode(User user, String code, UseStatus useStatus) {
        this.user = user;
        this.code = code;
        this.useStatus = useStatus;
    }

    public void checkUsed(){
        if(useStatus.equals(UseStatus.USED)) throw new LuckKidsException(ErrorCode.FRIEND_CODE_USED);
    }

    public void updateUseStatus(){
        this.useStatus = UseStatus.USED;
    }
}
