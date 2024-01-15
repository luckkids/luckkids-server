package com.luckkids.api.service.userCharacter;

import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.userCharacter.request.UserCharacterCreateServiceRequest;
import com.luckkids.api.service.userCharacter.response.UserCharacterCreateResponse;
import com.luckkids.domain.user.User;
import com.luckkids.domain.userCharacter.UserCharacter;
import com.luckkids.domain.userCharacter.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCharacterService {

    private final UserCharacterRepository userCharacterRepository;
    private final UserReadService userReadService;
    private final SecurityService securityService;

    public UserCharacterCreateResponse createUserCharacter(UserCharacterCreateServiceRequest request) {
        int userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findByOne(userId);

        UserCharacter userCharacter = request.toEntity(user);
        return UserCharacterCreateResponse.of(userCharacterRepository.save(userCharacter));
    }

}
