package com.luckkids.docs.popup;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.popup.PopupController;
import com.luckkids.api.service.popup.PopupReadService;
import com.luckkids.api.service.popup.response.PopupButtonResponse;
import com.luckkids.api.service.popup.response.PopupResponse;
import com.luckkids.docs.RestDocsSupport;

public class PopupControllerDocsTest extends RestDocsSupport {

    private final PopupReadService popupReadService = mock(PopupReadService.class);

    @Override
    protected Object initController() {
        return new PopupController(popupReadService);
    }

    @DisplayName("최신 팝업 정보를 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void getLatestPopup() throws Exception {
        // given
        PopupButtonResponse button1 = PopupButtonResponse.builder()
                .bgColor("LUCK_GREEN")
                .link(null)
                .text("테스트 시작하기")
                .textColor("BLACK")
                .build();

        PopupButtonResponse button2 = PopupButtonResponse.builder()
                .bgColor("BG_TERTIARY")
                .link(null)
                .text("다음에 보기")
                .textColor("WHITE")
                .build();

        PopupResponse response = PopupResponse.builder()
                .label("개운법 테스트 OPEN!")
                .title("내 운이 트이는 방법은?")
                .description("운이 트이는, 나만의 개운법(開運法)을\n지금 확인해보세요!")
                .imageUrl("https://api-luckkids.kro.kr/api/v1/images/popup-test-sun.png")
                .buttons(List.of(button1, button2))
                .build();

        given(popupReadService.getLatestPopup()).willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/api/v1/popup")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("popup-latest",
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
                                fieldWithPath("data.label").type(JsonFieldType.STRING)
                                        .description("팝업 라벨"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("팝업 제목"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                        .description("팝업 설명"),
                                fieldWithPath("data.imageUrl").type(JsonFieldType.STRING)
                                        .description("팝업 이미지 URL"),
                                fieldWithPath("data.buttons[]").type(JsonFieldType.ARRAY)
                                        .description("팝업 버튼 목록"),
                                fieldWithPath("data.buttons[].bgColor").type(JsonFieldType.STRING)
                                        .description("버튼 배경색 (예: LUCK_GREEN, BG_TERTIARY)"),
                                fieldWithPath("data.buttons[].link").type(JsonFieldType.NULL)
                                        .description("버튼 클릭 시 이동할 링크 (없으면 null)"),
                                fieldWithPath("data.buttons[].text").type(JsonFieldType.STRING)
                                        .description("버튼 텍스트"),
                                fieldWithPath("data.buttons[].textColor").type(JsonFieldType.STRING)
                                        .description("버튼 텍스트 색상 (예: BLACK, WHITE)")
                        )
                ));
    }
}
