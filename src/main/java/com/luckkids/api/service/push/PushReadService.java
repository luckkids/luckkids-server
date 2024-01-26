package com.luckkids.api.service.push;

import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushReadService {

    private final PushRepository pushRepository;

    public Push findByDeviceIdAndUser(String deviceId, int userId){
        return pushRepository.findByDeviceIdAndUserId(deviceId,userId)
            .orElseThrow(() -> new IllegalArgumentException("Push가 존재하지 않습니다."));
    }
}
