package com.luckkids.api.service.version;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.controller.version.request.VersionSaveRequest;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mail.request.SendMailServiceRequest;
import com.luckkids.api.service.mail.response.SendMailResponse;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.domain.version.Version;
import com.luckkids.domain.version.VersionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class VersionReadServiceTest extends IntegrationTestSupport {

    @Autowired
    VersionReadService versionReadService;
    @Autowired
    VersionService versionService;
    @Autowired
    VersionRepository versionRepository;

    @DisplayName(value = "제일 최신버전을 가져온다.")
    @Test
    void getVersionTest() {
        Version version = Version.builder()
                .versionNum("1.1.2")
                .build();

        versionRepository.save(version);

        VersionResponse versionResponse = versionReadService.getVersion();

        assertThat(versionResponse.getVersionNum()).isEqualTo("1.1.2");
    }

    @DisplayName(value = "최신버전을 저장한다.")
    @Test
    void saveVersionTest() {
        VersionSaveServiceRequest versionSaveServiceRequest = VersionSaveServiceRequest.builder()
                .versionNum("1.1.2")
                .build();

        VersionSaveResponse versionSaveResponse = versionService.save(versionSaveServiceRequest);

        assertThat(versionSaveResponse.getVersionNum()).isEqualTo("1.1.2");
    }
}
