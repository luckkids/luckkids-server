package com.luckkids.api.controller.notice.request;

import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveRequest {
    @NotBlank(message = "공지사항 제목은 필수입니다.")
    private String title;
    @NotBlank(message = "공지사항 URL은 필수입니다.")
    private String url;

    @Builder
    private NoticeSaveRequest(String title, String url) {
        this.title = title;
        this.title = title;
        this.url = url;
    }

    public NoticeSaveServiceRequest toServiceRequest(){
        return NoticeSaveServiceRequest.builder()
            .title(title)
            .url(url)
            .build();
    }
}
