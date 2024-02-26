package com.luckkids.api.service.notice.request;

import com.luckkids.api.service.firebase.request.SendPushServiceRequest;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.push.Push;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveServiceRequest {

    private String title;
    private String noticeDescription;

    @Builder
    private NoticeSaveServiceRequest(String title, String noticeDescription) {
        this.title = title;
        this.noticeDescription = noticeDescription;
    }

    public Notice toEntity(){
        return Notice.builder()
            .title(title)
            .noticeDescription(noticeDescription)
            .build();
    }

    public SendPushServiceRequest toSendPushServiceRequest(Push push){
        return SendPushServiceRequest.builder()
            .title(title)
            .body(noticeDescription)
            .push(push)
            .screenName("NOTICE")
            .build();
    }
}
