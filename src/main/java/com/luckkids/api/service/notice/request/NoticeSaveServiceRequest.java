package com.luckkids.api.service.notice.request;

import com.luckkids.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveServiceRequest {

    private String title;
    private String url;

    @Builder
    private NoticeSaveServiceRequest(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Notice toEntity(){
        return Notice.builder()
            .title(title)
            .url(url)
            .build();
    }
}
