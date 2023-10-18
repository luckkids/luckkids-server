package com.luckkids;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocumentationResultHandler write(){
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", //snippet파일 클래스명으로 디렉토리구분
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), //요청 응답값 json정렬 자동
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }
}
