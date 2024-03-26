package com.luckkids.docs.push;

import com.luckkids.api.controller.notice.NoticeController;
import com.luckkids.api.controller.push.PushController;
import com.luckkids.api.controller.push.request.PushSoundChangeRequest;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.NoticeService;
import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.push.PushService;
import com.luckkids.api.service.push.request.PushSoundChangeServiceRequest;
import com.luckkids.api.service.push.response.PushSoundChangeResponse;
import com.luckkids.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PushControllerDocsTest extends RestDocsSupport {
    private final PushService pushService = mock(PushService.class);

    @Override
    protected Object initController() {
        return new PushController(pushService);
    }

    @DisplayName("푸시 사운드 수정 API")
    @Test
    @WithMockUser(roles = "USER")
    void changeSound() throws Exception {
        // given
        PushSoundChangeRequest request = PushSoundChangeRequest.builder()
            .deviceId("testDeviceId")
            .sound("sound.wav")
            .build();

        given(pushService.updateSound(any(PushSoundChangeServiceRequest.class)))
            .willReturn(
                PushSoundChangeResponse.builder()
                    .pushToken("testPushToken")
                    .deviceId("testDeviceId")
                    .sound("sound.wav")
                    .build()
            );

        // when // then
        mockMvc.perform(
                patch("/api/v1/push")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("change-sound",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("deviceId").type(JsonFieldType.STRING)
                        .description("디바이스ID"),
                    fieldWithPath("sound").type(JsonFieldType.STRING)
                        .description("사운드명")
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
                    fieldWithPath("data.pushToken").type(JsonFieldType.STRING)
                        .description("푸시토큰"),
                    fieldWithPath("data.deviceId").type(JsonFieldType.STRING)
                        .description("디바이스ID"),
                    fieldWithPath("data.sound").type(JsonFieldType.STRING)
                        .description("사운드명")
                )
            ));
    }
}
