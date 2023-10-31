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
    private String noticeDescription;
    private LocalDateTime createDate;

    @Builder
    private NoticeResponse(int id, String noticeDescription, LocalDateTime createDate) {
        this.id = id;
        this.noticeDescription = noticeDescription;
        this.createDate = createDate;
    }

    public static NoticeResponse of(Notice notice){
        return NoticeResponse.builder()
                .id(notice.getId())
                .noticeDescription(notice.getNoticeDescription())
                .createDate(notice.getCreatedDate())
                .build();
    }

}
