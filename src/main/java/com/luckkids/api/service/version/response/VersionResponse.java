package com.luckkids.api.service.version.response;

import com.luckkids.domain.version.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionResponse {

    private int id;
    private String versionNum;

    @Builder
    private VersionResponse(int id, String versionNum) {
        this.id = id;
        this.versionNum = versionNum;
    }

    public static VersionResponse of(Version version){
        return VersionResponse.builder()
                .id(version.getId())
                .versionNum(version.getVersionNum())
                .build();
    }
}
