package com.luckkids.domain.userAgreement;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgreementStatus {
    AGREE("동의"),
    DISAGREE("비동의");

    private final String text;
}
