package com.luckkids.docs.friendCode;

import com.luckkids.api.controller.friend.request.FriendCreateRequest;
import com.luckkids.api.controller.friendCode.FriendCodeController;
import com.luckkids.api.service.friendCode.FriendCodeService;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendCodeControllerDocsTest extends RestDocsSupport {

    private final FriendCodeService friendCodeService = mock(FriendCodeService.class);

    @Override
    protected Object initController() {
        return new FriendCodeController(friendCodeService);
    }

    @DisplayName("친구코드 생성 API")
    @Test
    @WithMockUser(roles = "USER")
    void friendcode() throws Exception {
        // given
        given(friendCodeService.inviteCode())
            .willReturn(FriendInviteCodeResponse.builder()
                .code("ACSDSWEE")
                .build()
            );
        // when // then
        mockMvc.perform(
                get("/api/v1/friendcode")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-friendcode",
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
                    fieldWithPath("data.code").type(JsonFieldType.STRING)
                        .description("친구 코드")
                )
            ));
    }

    @DisplayName("친구코드로 친구추가 API")
    @Test
    @WithMockUser(roles = "USER")
    void createFriend() throws Exception {
        // given
        FriendCreateRequest request = FriendCreateRequest.builder()
            .code("ASDSDWEE")
            .build();

        given(friendCodeService.create(any(FriendCreateServiceRequest.class)))
            .willReturn(FriendCreateResponse.builder()
                .receiver("skhan@test.com")
                .requester("ghmin@test.com")
                .build()
            );

        // when // then
        mockMvc.perform(
                post("/api/v1/friendcode/create")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("create-friendcode",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("친구코드")
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
                    fieldWithPath("data.receiver").type(JsonFieldType.STRING)
                        .description("초대 받은 친구"),
                    fieldWithPath("data.requester").type(JsonFieldType.STRING)
                        .description("초대한 친구")
                )
            ));
    }
}
