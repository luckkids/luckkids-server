package com.luckkids.api.service.version;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VersionServiceTest extends IntegrationTestSupport {

    @Autowired
    private VersionService versionService;

    @Autowired
    private VersionRepository versionRepository;

    @AfterEach
    void tearDown() {
        versionRepository.deleteAllInBatch();
    }

    @DisplayName(value = "최신버전을 저장한다.")
    @Test
    void saveVersionTest() {
        VersionSaveServiceRequest versionSaveServiceRequest = VersionSaveServiceRequest.builder()
            .versionNum("1.1.2")
            .build();

        VersionSaveResponse versionSaveResponse = versionService.save(versionSaveServiceRequest);

        Version version = versionRepository.findById(versionSaveResponse.getId()).get();

        assertThat(version.getVersionNum()).isEqualTo("1.1.2");
    }
}
