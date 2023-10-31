package com.luckkids.api.controller.version.request;

import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveRequest {
    private String versionNum;

    @Builder
    private VersionSaveRequest(String versionNum) {
        this.versionNum = versionNum;
    }

    public VersionSaveServiceRequest toServiceRequest(){
        return VersionSaveServiceRequest.builder()
                .versionNum(versionNum)
                .build();
    }
}
