package com.luckkids.api.service.version;

import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VersionService {

    private final VersionRepository versionRepository;
    private final PushService pushService;

    public VersionSaveResponse save(VersionSaveServiceRequest versionSaveServiceRequest){
        Version savedVersion = versionRepository.save(versionSaveServiceRequest.toEntity());
        pushService.sendPushAlertType(versionSaveServiceRequest.toSendPushAlertTypeRequest(AlertType.NOTICE));
        return VersionSaveResponse.of(savedVersion);
    }
}
