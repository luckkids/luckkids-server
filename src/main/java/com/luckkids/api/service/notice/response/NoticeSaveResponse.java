package com.luckkids.api.service.notice.response;

import com.luckkids.domain.notice.Notice;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveResponse {
    private int id;
    private String noticeDescription;

    @Builder
    private NoticeSaveResponse(int id, String noticeDescription) {
        this.id = id;
        this.noticeDescription = noticeDescription;
    }

    public static NoticeSaveResponse of(Notice notice){
        return NoticeSaveResponse.builder()
            .id(notice.getId())
            .noticeDescription(notice.getNoticeDescription())
            .build();
    }
}
