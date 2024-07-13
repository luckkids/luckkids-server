package com.luckkids.api.service.push.request;

import com.luckkids.api.service.firebase.request.SendFirebaseDataDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendPushDataDto {

    private String alert_destination_type;
    private String alert_destination_info;

    @Builder
    private SendPushDataDto(String alert_destination_type, String alert_destination_info) {
        this.alert_destination_type = alert_destination_type;
        this.alert_destination_info = alert_destination_info;
    }

    public SendFirebaseDataDto toFirebaseDataDto(){
        return SendFirebaseDataDto.builder()
                .alert_destination_type(alert_destination_type)
                .alert_destination_info(alert_destination_info)
                .build();
    }

}
