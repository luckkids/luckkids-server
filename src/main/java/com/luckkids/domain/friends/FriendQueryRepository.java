package com.luckkids.domain.friends;

import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.domain.friends.projection.FriendProfileReadDto;
import org.springframework.data.domain.Page;

public interface FriendQueryRepository {

    public Page<FriendListReadResponse> readListFriend(int userId, PageInfoServiceRequest page);
    public FriendProfileReadDto readProfile(int friendId);
}
