package com.luckkids.domain.friends;

import com.luckkids.api.service.friend.response.FriendListReadServiceResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadServiceResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import org.springframework.data.domain.Page;

public interface FriendRepositoryCustom {

    public Page<FriendListReadServiceResponse> readListFriend(int userId, PageInfoServiceRequest page);
    public FriendProfileReadServiceResponse readProfile(int friendId);
}
