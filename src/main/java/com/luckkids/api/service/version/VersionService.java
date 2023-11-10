package com.luckkids.api.service.version;

import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    public VersionSaveResponse save(VersionSaveServiceRequest versionSaveServiceRequest){
        Version savedVersion = versionRepository.save(versionSaveServiceRequest.toEntity());
        return VersionSaveResponse.of(savedVersion);
    }
}
