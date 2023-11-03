package com.luckkids.api.service.notice.response;

import com.luckkids.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeDetailResponse {
    private int id;
    private String title;
    private String noticeDescription;
    private LocalDateTime createdDate;

    @Builder
    private NoticeDetailResponse(int id, String title, String noticeDescription, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.noticeDescription = noticeDescription;
        this.createdDate = createdDate;
    }

    public static NoticeDetailResponse of(Notice notice){
        return NoticeDetailResponse.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .noticeDescription(notice.getNoticeDescription())
            .createdDate(notice.getCreatedDate())
            .build();
    }
}
