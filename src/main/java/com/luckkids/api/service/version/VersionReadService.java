package com.luckkids.api.service.version;

import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VersionReadService {

    private final VersionRepository versionRepository;

    public VersionResponse getVersion(){
        Version version = versionRepository.findVersionMax();
        return VersionResponse.of(version);
    }
}
