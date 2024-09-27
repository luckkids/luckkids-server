package com.luckkids.api.service.friend;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.service.friend.request.FriendStatusRequest;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendQueryRepository;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.user.UserQueryRepository;
import com.luckkids.domain.user.projection.MyProfileDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendReadService {

	private final FriendQueryRepository friendQueryRepository;
	private final UserQueryRepository userQueryRepository;

	private final SecurityService securityService;
	private final FriendRepository friendRepository;

	public FriendListResponse getFriendList(PageInfoServiceRequest pageRequest) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		Pageable pageable = pageRequest.toPageable();

		MyProfileDto myProfile = userQueryRepository.getMyProfile(userId);
		Page<FriendProfileDto> friendPagingList = friendQueryRepository.getFriendList(userId, pageable);

		return FriendListResponse.of(myProfile, PageCustom.of(friendPagingList));
	}

	public boolean checkFriendStatus(FriendStatusRequest friendStatusRequest) {
		Optional<Friend> friend = friendRepository.findByRequesterIdAndReceiverId(friendStatusRequest.getRequestId(),
			friendStatusRequest.getReceiverId());
		return friend.isPresent();
	}

	public Set<Integer> getFriendIds(int userId) {
		return friendQueryRepository.findFriendIds(userId);
	}
}
