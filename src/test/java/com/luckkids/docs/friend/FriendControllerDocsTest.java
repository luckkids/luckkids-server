package com.luckkids.docs.friend;

import com.luckkids.api.controller.friend.FriendController;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.FriendService;
import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.request.PageInfoServiceRequest;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.api.service.response.PageableCustom;
import com.luckkids.docs.RestDocsSupport;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendControllerDocsTest extends RestDocsSupport {

    private final FriendReadService friendReadService = mock(FriendReadService.class);
    private final FriendService friendService = mock(FriendService.class);

    @Override
    protected Object initController() {
        return new FriendController(friendReadService, friendService);
    }

    @DisplayName("친구랭킹목록 API")
    @Test
    @WithMockUser(roles = "USER")
    void readFriend() throws Exception {
        // given
        List<FriendListReadResponse> friendList = Arrays.asList(
            FriendListReadResponse.builder()
                .friendId(1)
                .characterName("캐릭터이름1")
                .fileUrl("http://example.com/file1")
                .missionCount(10)
                .build(),
            FriendListReadResponse.builder()
                .friendId(2)
                .characterName("캐릭터이름2")
                .fileUrl("http://example.com/file2")
                .missionCount(20)
                .build()
        );

        PageableCustom pageableCustom = PageableCustom.builder()
            .totalElements(10)
            .totalPages(1)
            .currentPage(1)
            .build();

        PageCustom<FriendListReadResponse> pageCustom = PageCustom.<FriendListReadResponse>builder()
            .content(friendList)
            .pageInfo(pageableCustom)
            .build();

        given(friendReadService.readListFriend(any(PageInfoServiceRequest.class)))
            .willReturn(pageCustom);

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/list")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("read-friend",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("page")
                        .description("페이지 기본값(1)")
                        .optional(),
                    parameterWithName("size")
                        .description("페이지 사이즈 기본값(10)")
                        .optional()
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
                    fieldWithPath("data.content[].missionCount").type(JsonFieldType.NUMBER)
                        .description("미션갯수"),
                    fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                        .description("현재페이지"),
                    fieldWithPath("data.pageInfo.totalPages").type(JsonFieldType.NUMBER)
                        .description("총페이지수"),
                    fieldWithPath("data.pageInfo.totalElements").type(JsonFieldType.NUMBER)
                        .description("총 리스트개수")
                )
            ));
    }

    @DisplayName("친구 프로필 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void readFriendProfile() throws Exception {
        // given
        given(friendReadService.readProfile(anyInt()))
            .willReturn(FriendProfileReadResponse.builder()
                .phraseDescription("행운문구!!")
                .characterName("캐릭터이름")
                .level(10)
                .fileUrl("https://test.com/file")
                .build()
            );

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/profile/{friendId}", 1)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("friend-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("friendId")
                        .description("친구 ID")
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
                    fieldWithPath("data.phraseDescription").type(JsonFieldType.STRING)
                        .description("친구 행운문구"),
                    fieldWithPath("data.fileUrl").type(JsonFieldType.STRING)
                        .description("친구 캐릭터 파일URL"),
                    fieldWithPath("data.characterName").type(JsonFieldType.STRING)
                        .description("친구 캐릭터 명"),
                    fieldWithPath("data.level").type(JsonFieldType.NUMBER)
                        .description("친구 레벨")
                )
            ));
    }
}
