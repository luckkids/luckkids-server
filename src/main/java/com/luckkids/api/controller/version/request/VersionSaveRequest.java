package com.luckkids.api.controller.version.request;

import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveRequest {

    @NotBlank(message = "최신버전은 필수입니다.")
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
