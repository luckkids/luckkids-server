package com.luckkids.domain.user;

import com.luckkids.api.exception.LuckKidsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum SnsType {
    NORMAL("일반"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플");

    private final String text;

    public void checkSnsType(){
        Optional<SnsType> snsType = Arrays.stream(values())
            .filter(type -> type.getText().equals(text))
            .findFirst();
        snsType.ifPresent(type -> {
            throw new LuckKidsException(type.name());
        });
    }
}
