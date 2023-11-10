package com.luckkids.api.service.version.response;

import com.luckkids.domain.version.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveResponse {

    private int id;
    private String versionNum;

    @Builder
    private VersionSaveResponse(int id, String versionNum) {
        this.id = id;
        this.versionNum = versionNum;
    }

    public static VersionSaveResponse of(Version version){
        return VersionSaveResponse.builder()
                .id(version.getId())
                .versionNum(version.getVersionNum())
                .build();
    }
}
