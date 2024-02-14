package com.luckkids.api.service.join;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.join.request.JoinServiceRequest;
import com.luckkids.api.service.join.response.JoinResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinResponse joinUser(JoinServiceRequest joinServiceRequest) {
        try {
            User user = joinServiceRequest.toEntity();
            user.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            return JoinResponse.of(savedUser);
        } catch (Exception e) {
            throw new LuckKidsException(ErrorCode.USER_NORMAL);
        }
    }
}
