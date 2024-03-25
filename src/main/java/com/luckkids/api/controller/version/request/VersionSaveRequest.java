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
    @NotBlank(message = "최신버전은 URL은 필수입니다.")
    private String url;

    @Builder
    private VersionSaveRequest(String versionNum, String url) {
        this.versionNum = versionNum;
        this.url = url;
    }

    public VersionSaveServiceRequest toServiceRequest(){
        return VersionSaveServiceRequest.builder()
                .versionNum(versionNum)
                .url(url)
                .build();
    }
}
