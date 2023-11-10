package com.luckkids.api.service.notice.response;

import com.luckkids.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponse {
    private int id;
    private String title;
    private LocalDateTime createdDate;

    @Builder
    private NoticeResponse(int id, String title, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
    }

    public static NoticeResponse of(Notice notice){
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .createdDate(notice.getCreatedDate())
                .build();
    }

}
