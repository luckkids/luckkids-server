package com.luckkids.api.service.user;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserQueryRepository;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.domain.user.projection.UserLeagueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    private final SecurityService securityService;

    public User findByOne(int id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.USER_EMAIL));
    }

    public UserFindSnsTypeResponse findEmail(UserFindEmailServiceRequest userFindEmailServiceRequest) {
        return UserFindSnsTypeResponse.of(findByEmail(userFindEmailServiceRequest.getEmail()));
    }

    public List<UserLeagueResponse> getUserLeagueTop3() {
        return userQueryRepository.getUserLeagueTop3()
            .stream()
            .map(UserLeagueDto::toUserLeagueResponse)
            .collect(Collectors.toList());
    }

    public double getCharacterAchievementRate() {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = this.findByOne(userId);
        return user.calculateAchievementRate();
    }
}
