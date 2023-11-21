package com.luckkids.api.service.user;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.request.UserChangePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserChangePasswordResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserReadService userReadService;

    public UserChangePasswordResponse changePassword(UserChangePasswordServiceRequest userChangePasswordServiceRequest){
        User user = userReadService.findByEmail(userChangePasswordServiceRequest.getEmail());
        user.changePassword(userChangePasswordServiceRequest.getPassword());
        return UserChangePasswordResponse.of(user);
    }
}
