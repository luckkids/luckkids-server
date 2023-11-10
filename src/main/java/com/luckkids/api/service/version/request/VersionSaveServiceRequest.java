package com.luckkids.api.service.version.request;

import com.luckkids.domain.version.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveServiceRequest {
    private String versionNum;

    @Builder
    private VersionSaveServiceRequest(String versionNum) {
        this.versionNum = versionNum;
    }

    public Version toEntity(){
        return Version.builder()
                .versionNum(versionNum)
                .build();
    }
}
