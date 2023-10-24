package com.luckkids.api.service.join;

import com.luckkids.api.service.join.request.JoinCheckEmailServiceRequest;
import com.luckkids.api.service.join.response.JoinCheckEmailResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinReadService {

    private final UserRepository userRepository;

    public JoinCheckEmailResponse checkEmail(JoinCheckEmailServiceRequest joinCheckEmailServiceRequest){
        String email = joinCheckEmailServiceRequest.getEmail();
        User user =  userRepository.findByEmail(email);

        Optional.ofNullable(user).ifPresent(User::checkSnsType);

        return JoinCheckEmailResponse.of(email);
    }
}
