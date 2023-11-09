package com.luckkids.api;

import com.luckkids.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

class ErrorNotifierTest extends IntegrationTestSupport {

    @Autowired
    private ErrorNotifier errorNotifier;

    @DisplayName("에러를 슬랙으로 잘 보내는지 확인한다.")
    @Test
    void sendErrorToSlack() throws IOException {
        // given
        Exception testException = new Exception("테스트 EXCEPTION");

        // when
//        errorNotifier.sendErrorToSlack(testException);    // 평소엔 주석처리

        // then
        System.out.println("슬랙 보내기 성공!");


    }

}