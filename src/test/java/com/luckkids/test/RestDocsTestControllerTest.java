package com.luckkids.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.AbstractRestDocsTests;
import com.luckkids.api.controller.RestDocsTestController;
import com.luckkids.api.controller.dto.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestDocsTestController.class)
class RestDocsTestControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void RestDocsTest() throws Exception {

        TestRequest testRequest = new TestRequest("","");
        String requestJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(
                post("/restDocsTest")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
//                            responseFields(
//                                fieldWithPath("test1").description("테스트응답값1"),
//                                fieldWithPath("test2").description("테스트응답값2")
//                            ),
                            requestFields(
                                    fieldWithPath("test1").description("테스트요청값1"),
                                    fieldWithPath("test2").description("테스트요청값2")
                            )
                        )
                );
    }
}
