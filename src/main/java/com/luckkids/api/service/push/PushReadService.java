package com.luckkids.api.service.push;

import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushQueryRepository;
import com.luckkids.domain.push.PushRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushReadService {

    private final PushRepository pushRepository;
    private final PushQueryRepository pushQueryRepository;

    public Push findByDeviceIdAndUser(String deviceId, int userId){
        return pushRepository.findByDeviceIdAndUserId(deviceId,userId)
            .orElseThrow(() -> new IllegalArgumentException("Push가 존재하지 않습니다."));
    }

    public List<Push> findAllByAlertType(AlertType alertType){
        return pushQueryRepository.findAllByAlertType(alertType);
    }
}
