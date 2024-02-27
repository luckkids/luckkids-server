package com.luckkids.api.service.version.request;

import com.luckkids.api.service.push.request.SendPushAlertTypeServiceRequest;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.push.PushMessage;
import com.luckkids.domain.version.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VersionSaveServiceRequest {
    private String versionNum;

    @Builder
    private VersionSaveServiceRequest(String versionNum) {
        this.versionNum = versionNum;
    }

    public Version toEntity(){
        return Version.builder()
                .versionNum(versionNum)
                .build();
    }

    public SendPushAlertTypeServiceRequest toSendPushAlertTypeRequest(AlertType alertType){
        return SendPushAlertTypeServiceRequest.builder()
            .alertType(alertType)
            .body(PushMessage.APP_UPDATE.getText())
            .screenName("NOTICE")
            .build();
    }
}
