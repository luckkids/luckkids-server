package com.luckkids.api.service.user;

import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.domain.user.User;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserReadService userReadService;
    private final SecurityService securityService;

    public UserUpdatePasswordResponse updatePassword(UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest) {
        User user = userReadService.findByEmail(userUpdatePasswordServiceRequest.getEmail());
        user.updatePassword(userUpdatePasswordServiceRequest.getPassword());
        return UserUpdatePasswordResponse.of(user);
    }

    public UserLuckPhrasesResponse updatePhrase(UserLuckPhrasesServiceRequest userLuckPhrasesServiceRequest){
        int userId = securityService.getCurrentUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        user.updateLuckPhrases(userLuckPhrasesServiceRequest.getLuckPhrases());
        return UserLuckPhrasesResponse.of(user);
    }
}