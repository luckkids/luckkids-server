package com.luckkids.api.service.user;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserReadService {

    private final UserRepository userRepository;

    public User findByOne(int id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + id));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.USER_EMAIL));
    }

}
