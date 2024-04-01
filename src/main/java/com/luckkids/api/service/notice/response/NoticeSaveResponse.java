package com.luckkids.api.service.notice.response;

import com.luckkids.domain.notice.Notice;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeSaveResponse {
    private int id;
    private String title;
    private String url;
    private LocalDateTime createdDate;

    @Builder
    private NoticeSaveResponse(int id, String title, String url, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.createdDate = createdDate;
    }

    public static NoticeSaveResponse of(Notice notice){
        return NoticeSaveResponse.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .url(notice.getUrl())
            .createdDate(notice.getCreatedDate())
            .build();
    }
}
