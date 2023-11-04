package com.luckkids.domain.friends;

import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import org.springframework.data.domain.Page;

public interface FriendQueryRepository {

    public Page<FriendListReadResponse> readListFriend(int userId, PageInfoServiceRequest page);
    public FriendProfileReadResponse readProfile(int friendId);
}
