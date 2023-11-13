package com.luckkids.domain.friends;

import com.luckkids.domain.friends.projection.FriendListDto;
import com.luckkids.domain.friends.projection.FriendProfileReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendQueryRepository {

    Page<FriendListDto> getFriendsList(int userId, Pageable pageable);

    FriendProfileReadDto readProfile(int friendId);

}
