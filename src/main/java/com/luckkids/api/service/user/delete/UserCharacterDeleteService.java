package com.luckkids.api.service.user.delete;

import com.luckkids.domain.userCharacter.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCharacterDeleteService {

    private final UserCharacterRepository userCharacterRepository;

    public void deleteAllByUserId(int userId) {
        userCharacterRepository.deleteAllByUserId(userId);
    }
}
