package com.luckkids.api.service.friendCode;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendCodeReadService {
    private final FriendCodeRepository friendCodeRepository;

    public FriendCode findByCode(String code){
        return friendCodeRepository.findByCode(code)
            .orElseThrow(() -> new LuckKidsException(ErrorCode.FRIEND_CODE_UNKNOWN));
    }
}
