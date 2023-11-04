package com.luckkids.docs.notice;

import com.luckkids.api.controller.notice.NoticeController;
import com.luckkids.api.controller.notice.request.NoticeSaveRequest;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.NoticeService;
import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.notice.response.NoticeDetailResponse;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NoticeControllerDocsTest extends RestDocsSupport {

    private final NoticeReadService noticeReadService = mock(NoticeReadService.class);
    private final NoticeService noticeService = mock(NoticeService.class);

    @Override
    protected Object initController() {
        return new NoticeController(noticeReadService, noticeService);
    }

    @DisplayName("등록되어있는 공지사항목록을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void findListNotice() throws Exception {
        // given
        given(noticeReadService.getNoticeList())
            .willReturn(List.of(
                createNoticeResponse(1, "공지사항 타이틀1"),
                createNoticeResponse(2, "공지사항 타이틀2")
            ));

        // when // then
        mockMvc.perform(
                get("/api/v1/notice/list")
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("notice-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                        .description("코드"),
                    fieldWithPath("httpStatus").type(JsonFieldType.STRING)
                        .description("상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메세지"),
                    fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                        .description("응답 데이터"),
                    fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                        .description("공지사항 ID"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("data[].createdDate").type(JsonFieldType.STRING)
                        .description("공지사항 생성시간")
                )
            ));
    }

    @DisplayName("등록되어있는 공지사항을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void findOneNotice() throws Exception {
        // given
        given(noticeReadService.getNotice(anyInt()))
            .willReturn(
                NoticeDetailResponse.builder()
                    .id(1)
                    .title("공지사항 타이틀")
                    .noticeDescription("공지사항 내용")
                    .createdDate(LocalDateTime.now())
                    .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/notice/{id}",1)
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("notice-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("id")
                                .description("공지사항 ID")
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
                        .description("공지사항 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("data.noticeDescription").type(JsonFieldType.STRING)
                        .description("공지사항 내용"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("공지사항 생성시간")
                )
            ));
    }

    @DisplayName("공지사항을 등록하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void saveNotice() throws Exception {
        // given
        NoticeSaveRequest noticeSaveRequest = NoticeSaveRequest.builder()
            .title("공지사항 제목")
            .noticeDescription("공지사항 내용")
            .build();

        given(noticeService.saveNotice(any(NoticeSaveServiceRequest.class)))
            .willReturn(
                NoticeSaveResponse.builder()
                    .id(1)
                    .title("공지사항 타이틀")
                    .noticeDescription("공지사항 내용")
                    .createdDate(LocalDateTime.now())
                    .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/notice/save")
                    .content(objectMapper.writeValueAsString(noticeSaveRequest))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("notice-save",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("noticeDescription").type(JsonFieldType.STRING)
                        .description("공지사항 내용")
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
                        .description("공지사항 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING)
                        .description("공지사항 제목"),
                    fieldWithPath("data.noticeDescription").type(JsonFieldType.STRING)
                        .description("공지사항 내용"),
                    fieldWithPath("data.createdDate").type(JsonFieldType.STRING)
                        .description("공지사항 생성시간")
                )
            ));
    }

    public NoticeResponse createNoticeResponse(int id, String title){
        return NoticeResponse.builder()
            .id(id)
            .title(title)
            .createdDate(LocalDateTime.now())
            .build();
    }

}
