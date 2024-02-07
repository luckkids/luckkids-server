package com.luckkids.api.service.version;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class VersionReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private VersionReadService versionReadService;

    @Autowired
    private VersionService versionService;  // ⭐️ 이 부분은 테스트 코드 (Read) 분리하는 게 좋을 것 같습니다 !

    @Autowired
    private VersionRepository versionRepository;

    @AfterEach
    void tearDown() {
        versionRepository.deleteAllInBatch();
    }

    @DisplayName(value = "제일 최신버전을 가져온다.")
    @Test
    void getVersionTest() {
        Version version1 = Version.builder()
            .versionNum("1.1.2")
            .build();

        Version version2 = Version.builder()
            .versionNum("1.1.3")
            .build();

        versionRepository.save(version1);
        versionRepository.save(version2);

        VersionResponse versionResponse = versionReadService.getVersion();

        assertThat(versionResponse.getVersionNum()).isEqualTo("1.1.3");
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
