package com.luckkids.docs.version;

import com.luckkids.api.controller.version.VersionController;
import com.luckkids.api.controller.version.request.VersionSaveRequest;
import com.luckkids.api.service.version.VersionReadService;
import com.luckkids.api.service.version.VersionService;
import com.luckkids.api.service.version.request.VersionSaveServiceRequest;
import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VersionControllerDocsTest extends RestDocsSupport {

    private final VersionReadService versionReadService = mock(VersionReadService.class);
    private final VersionService versionService = mock(VersionService.class);

    @Override
    protected Object initController() {
        return new VersionController(versionReadService, versionService);
    }

    @DisplayName("현재 등록되어있는 최신버전을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void findVersion() throws Exception {
        // given

        given(versionReadService.getVersion())
            .willReturn(
                VersionResponse.builder()
                    .id(1)
                    .versionNum("1.1.2")
                    .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/versions/")
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("version-get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("버전 ID"),
                    fieldWithPath("data.versionNum").type(JsonFieldType.STRING)
                        .description("버전")
                )
            ));
    }

    @DisplayName("버전을 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void saveVersion() throws Exception {
        // given
        VersionSaveRequest versionSaveRequest = VersionSaveRequest.builder()
            .versionNum("1.1.2")
            .url("www.test.com")
            .build();

        given(versionService.save(any(VersionSaveServiceRequest.class)))
            .willReturn(
                VersionSaveResponse.builder()
                    .id(1)
                    .versionNum("1.1.2")
                    .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/versions/new")
                    .content(objectMapper.writeValueAsString(versionSaveRequest))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("version-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("versionNum").type(JsonFieldType.STRING)
                        .description("최신버전"),
                    fieldWithPath("url").type(JsonFieldType.STRING)
                            .description("최신버전 소개 URL")
                ),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("버전 ID"),
                    fieldWithPath("data.versionNum").type(JsonFieldType.STRING)
                        .description("버전")
                )
            ));
    }
}
