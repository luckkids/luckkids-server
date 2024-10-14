package com.luckkids.domain.push;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushMessage {

    TITLE("luckkids 럭키즈\uD83C\uDF40"),
    DEFAULT_SOUND("noti_luckluck.wav"),
    MISSION("새로운 습관이 추가되었어요!"),
    UPDATE("럭키즈의 새로워진 기능을 소개할게요!"),
    APP_UPDATE("럭키즈 새 버전 출시! 지금 구경갈래요?"),
    LUCK_CONTENTS("행운을 키우는 습관의 비하인드 스토리를 공개합니다."),
    GARDEN("{nickName}님이 나를 가든에 추가했어요!"),
    FRIEND_CODE("{nickName}님이 친구 초대를 보냈어요!");

    private final String text;

}
