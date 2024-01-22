package com.luckkids.api.service.friend.response;

import com.luckkids.api.page.response.PageCustom;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.user.projection.MyProfileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendListResponse {
    private MyProfileDto myProfile;
    private PageCustom<FriendProfileDto> friendList;

    @Builder
    private FriendListResponse(MyProfileDto myProfile, PageCustom<FriendProfileDto> friendList) {
        this.myProfile = myProfile;
        this.friendList = friendList;
    }

    public static FriendListResponse of(MyProfileDto myProfile, PageCustom<FriendProfileDto> friendList) {
        return FriendListResponse.builder()
            .myProfile(myProfile)
            .friendList(friendList)
            .build();
    }
}
