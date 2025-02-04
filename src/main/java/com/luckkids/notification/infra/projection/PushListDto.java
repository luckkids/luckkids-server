package com.luckkids.notification.infra.projection;


public record PushListDto (
    String deviceId,
    String pushToken) {

}
