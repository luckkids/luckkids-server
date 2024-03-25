package com.luckkids.api.service.notice.request;

import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.api.service.push.request.SendPushDataDto;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.push.PushScreenName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveServiceRequest {

    private String title;
    private String noticeDescription;
    private String url;

    @Builder
    private NoticeSaveServiceRequest(String title, String noticeDescription, String url) {
        this.title = title;
        this.noticeDescription = noticeDescription;
        this.url = url;
    }

    public Notice toEntity(){
        return Notice.builder()
            .title(title)
            .noticeDescription(noticeDescription)
            .build();
    }

    public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType){
        return SendPushAlertTypeServiceRequest.builder()
            .alertType(alertType)
            .body(title)
            .sendPushDataDto(
                    SendPushDataDto.builder()
                            .screenName(PushScreenName.WEBVIEW.getText())
                            .url(url)
                            .build()
            )
            .build();
    }
}
