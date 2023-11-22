package com.luckkids.api.service.user;

import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserReadService userReadService;

    public UserUpdatePasswordResponse updatePassword(UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest){
        User user = userReadService.findByEmail(userUpdatePasswordServiceRequest.getEmail());
        user.changePassword(userUpdatePasswordServiceRequest.getPassword());
        return UserUpdatePasswordResponse.of(user);
    }
}
