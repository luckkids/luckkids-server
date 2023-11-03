package com.luckkids.docs.friend;

import com.luckkids.api.controller.friend.FriendController;
import com.luckkids.api.controller.request.PageInfoRequest;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.response.FriendListReadServiceResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.api.service.response.PageableCustom;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendControllerDocsTest extends RestDocsSupport {

    private final FriendReadService friendReadService = mock(FriendReadService.class);
    private final SecurityService securityService = mock(SecurityService.class);

    @Override
    protected Object initController() {
        return new FriendController(friendReadService, securityService);
    }

    @DisplayName("친구랭킹목록 API")
    @Test
    @WithMockUser(roles = "USER")
    void readFriend() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
            .page(1)
            .size(10)
            .build();

        List<FriendListReadServiceResponse> friendList = Arrays.asList(
            FriendListReadServiceResponse.builder()
                .friendId(1)
                .characterName("캐릭터이름1")
                .fileUrl("http://example.com/file1")
                .cloverCount(10)
                .build(),
            FriendListReadServiceResponse.builder()
                .friendId(2)
                .characterName("캐릭터이름2")
                .fileUrl("http://example.com/file2")
                .cloverCount(20)
                .build()
        );

        PageableCustom pageableCustom = PageableCustom.builder()
            .totalElements(10)
            .totalPages(1)
            .currentPage(1)
            .build();

        PageCustom<FriendListReadServiceResponse> pageCustom = PageCustom.<FriendListReadServiceResponse>builder()
            .content(friendList)
            .pageInfo(pageableCustom)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        given(friendReadService.readListFriend(anyInt(),any(PageInfoServiceRequest.class)))
            .willReturn(pageCustom);

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/list")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("read-friend",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("size").type(JsonFieldType.NUMBER)
                        .description("페이지사이즈"),
                    fieldWithPath("page").type(JsonFieldType.NUMBER)
                        .description("페이지")
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
                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                        .description("친구 랭킹 리스트"),
                    fieldWithPath("data.content[].friendId").type(JsonFieldType.NUMBER)
                        .description("친구 ID"),
                    fieldWithPath("data.content[].characterName").type(JsonFieldType.STRING)
                        .description("캐릭터이름"),
                    fieldWithPath("data.content[].fileUrl").type(JsonFieldType.STRING)
                        .description("캐릭터 파일"),
                    fieldWithPath("data.content[].cloverCount").type(JsonFieldType.NUMBER)
                        .description("클로버갯수"),
                    fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                        .description("현재페이지"),
                    fieldWithPath("data.pageInfo.totalPages").type(JsonFieldType.NUMBER)
                        .description("총페이지수"),
                    fieldWithPath("data.pageInfo.totalElements").type(JsonFieldType.NUMBER)
                        .description("총 리스트개수")
                )
            ));
    }

    private UserInfo createUserInfo() {
        return UserInfo.builder()
            .userId(1)
            .email("")
            .build();
    }
}
