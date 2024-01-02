package com.luckkids.api.service.user;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.delete.UserDeleteService;
import com.luckkids.api.service.user.request.UserLuckPhrasesServiceRequest;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserLuckPhrasesResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.api.service.user.response.UserWithdrawResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserReadService userReadService;
    private final SecurityService securityService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final List<UserDeleteService> userDeleteServices;

    public UserUpdatePasswordResponse updatePassword(UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest) {
        User user = userReadService.findByEmail(userUpdatePasswordServiceRequest.getEmail());
        user.updatePassword(bCryptPasswordEncoder.encode(userUpdatePasswordServiceRequest.getPassword()));
        return UserUpdatePasswordResponse.of(user);
    }

    public UserLuckPhrasesResponse updatePhrase(UserLuckPhrasesServiceRequest userLuckPhrasesServiceRequest){
        int userId = securityService.getCurrentUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        user.updateLuckPhrases(userLuckPhrasesServiceRequest.getLuckPhrases());
        return UserLuckPhrasesResponse.of(user);
    }

    public UserWithdrawResponse withdraw(){
        int userId = securityService.getCurrentUserInfo().getUserId();
        User user = userReadService.findByOne(userId);
        for(UserDeleteService userDeleteService : userDeleteServices){
            userDeleteService.deleteAllByUserId(user.getId());
        }
        userRepository.deleteById(userId);
        return UserWithdrawResponse.of(userId);
    }
}
