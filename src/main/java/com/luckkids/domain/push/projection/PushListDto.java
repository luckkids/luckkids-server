package com.luckkids.domain.push.projection;


public record PushListDto (
    String deviceId,
    String pushToken) {

}
