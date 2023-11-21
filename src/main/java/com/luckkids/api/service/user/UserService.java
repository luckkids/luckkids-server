package com.luckkids.api.service.user;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserReadService userReadService;
    private final SecurityService securityService;

    public UserLuckPhrasesResponse update(UserLuckPhrasesServiceRequest userLuckPhrasesServiceRequest){
        int userId = securityService.getCurrentUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        user.changeLuckPhrases(userLuckPhrasesServiceRequest.getLuckPhrases());
        return UserLuckPhrasesResponse.of(user);
    }
}
