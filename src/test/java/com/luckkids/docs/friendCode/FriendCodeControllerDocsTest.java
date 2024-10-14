package com.luckkids.docs.friendCode;

import com.luckkids.api.controller.friendCode.FriendCodeController;
import com.luckkids.api.controller.friendCode.request.FriendCreateRequest;
import com.luckkids.api.service.friendCode.FriendCodeReadService;
import com.luckkids.api.service.friendCode.FriendCodeService;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.friendCode.response.FriendRefuseResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.friendCode.FriendStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @DisplayName("친구코드 생성로 친구의 닉네임을 조회하는 API")
    @Test
    @WithMockUser(roles = "USER")
    void findNickNameByCode() throws Exception {
        // given
        given(friendCodeService.findNickNameByCode(any(FriendCodeNickNameServiceRequest.class)))
                .willReturn(FriendCodeNickNameResponse.builder()
                        .nickName("테스트 닉네임")
                        .status(FriendStatus.FRIEND)
                        .build()
                );
        // when // then
        mockMvc.perform(
                    get("/api/v1/friendcode/{code}/nickname", "ASDSDWEE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-friendCode-nickName",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("code")
                                        .description("친구 코드")
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
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING)
                                        .description("요청자 닉네임"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING)
                                        .description("친구 상태값 가능한 값:" + Arrays.toString(FriendStatus.values()))
                        )
                ));
    }

    @DisplayName("친구요청 거절 API")
    @Test
    @WithMockUser(roles = "USER")
    void refuseFriend() throws Exception {
        // given
        given(friendCodeService.refuseFriend(any(String.class)))
                .willReturn(FriendRefuseResponse.builder()
                        .code("ASDSDWEE")
                        .build()
                );
        // when // then
        mockMvc.perform(
                        post("/api/v1/friendcode/{code}/refuse", "ASDSDWEE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("refuse-friendCode",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("code")
                                        .description("친구 코드")
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
                                fieldWithPath("data.code").type(JsonFieldType.STRING)
                                        .description("거절한 친구코드")
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
