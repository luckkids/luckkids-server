package com.luckkids.api.service.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.api.service.user.response.UserResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserQueryRepository;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.user.projection.InProgressCharacterDto;
import com.luckkids.domain.user.projection.UserLeagueDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {

	private final UserRepository userRepository;
	private final UserQueryRepository userQueryRepository;
	private final SecurityService securityService;
	private final FriendReadService friendReadService;

	public User findByOne(int id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + id));
	}

	public UserResponse findByMe() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return UserResponse.of(findByOne(userId), getInProgressCharacter(userId));
	}

	public UserResponse findById(int userId) {
		return UserResponse.of(findByOne(userId), getInProgressCharacter(userId));
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new LuckKidsException(ErrorCode.USER_EMAIL));
	}

	public UserFindSnsTypeResponse findEmail(UserFindEmailServiceRequest userFindEmailServiceRequest) {
		return UserFindSnsTypeResponse.of(findByEmail(userFindEmailServiceRequest.getEmail()));
	}

	public List<UserLeagueResponse> getUserLeagueTop3() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		List<UserLeagueDto> userLeagueTop3 = userQueryRepository.getUserLeagueTop3();
		Set<Integer> friendIds = friendReadService.getFriendIds(userId);

		return userLeagueTop3.stream()
			.map(userLeagueDto -> friendIds.contains(userLeagueDto.getId())
				? UserLeagueResponse.withNickname(userLeagueDto)
				: UserLeagueResponse.withoutNickname(userLeagueDto))
			.collect(Collectors.toList());
	}

	public double getCharacterAchievementRate() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		User user = this.findByOne(userId);
		return user.calculateAchievementRate();
	}

	private InProgressCharacterDto getInProgressCharacter(int userId) {
		return userQueryRepository.getInProgressCharacter(userId);
	}
}
