package com.luckkids.docs.garden;

import com.luckkids.api.controller.garden.GardenController;
import com.luckkids.api.page.request.PageInfoRequest;
import com.luckkids.api.page.request.PageInfoServiceRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.response.UserLeagueResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.friend.projection.FriendProfileDto;
import com.luckkids.domain.user.projection.MyProfileDto;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GardenControllerDocsTest extends RestDocsSupport {

    private final FriendReadService friendReadService = mock(FriendReadService.class);
    private final UserReadService userReadService = mock(UserReadService.class);

    @Override
    protected Object initController() {
        return new GardenController(friendReadService, userReadService);
    }

    @DisplayName("친구 목록 조회 API")
    @Test
    @WithMockUser(roles = "USER")
    void getFriendList() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
            .page(1)
            .size(12)
            .build();

        MyProfileDto myProfile = new MyProfileDto(1, "럭키즈", "행운문구", "https://test.cloudfront.net/example.png", 1);

        List<FriendProfileDto> friendProfileList = List.of(
            new FriendProfileDto(2, "럭키즈 친구 2", "행운 문구 2", "https://test.cloudfront.net/example.png", 120),
            new FriendProfileDto(3, "럭키즈 친구 3", "행운 문구 3", "https://test.cloudfront.net/example.png", 90),
            new FriendProfileDto(4, "럭키즈 친구 4", "행운 문구 4", "https://test.cloudfront.net/example.png", 20)
        );

        PageableCustom pageInfo = PageableCustom.builder()
            .currentPage(1)
            .totalPage(1)
            .totalElement(10)
            .build();

        PageCustom<FriendProfileDto> friendList = PageCustom.<FriendProfileDto>builder()
            .content(friendProfileList)
            .pageInfo(pageInfo)
            .build();

        FriendListResponse response = FriendListResponse.builder()
            .myProfile(myProfile)
            .friendList(friendList)
            .build();

        given(friendReadService.getFriendList(any(PageInfoServiceRequest.class)))
            .willReturn(response);

        // when // then
        mockMvc.perform(
                get("/api/v1/garden/list")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("garden-list",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("page")
                        .description("페이지. 기본값: 1")
                        .optional(),
                    parameterWithName("size")
                        .description("페이지 사이즈. 기본값: 12")
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
                    fieldWithPath("data.myProfile").type(JsonFieldType.OBJECT)
                        .description("내 프로필 정보"),
                    fieldWithPath("data.myProfile.myId").type(JsonFieldType.NUMBER)
                        .description("내 ID"),
                    fieldWithPath("data.myProfile.nickname").type(JsonFieldType.STRING)
                        .description("내 닉네임"),
                    fieldWithPath("data.myProfile.luckPhrase").type(JsonFieldType.STRING)
                        .description("내 행운 문구"),
                    fieldWithPath("data.myProfile.imageFileUrl").type(JsonFieldType.STRING)
                        .description("내 캐릭터 URL"),
                    fieldWithPath("data.myProfile.characterCount").type(JsonFieldType.NUMBER)
                        .description("내 캐릭터 개수"),
                    fieldWithPath("data.friendList").type(JsonFieldType.OBJECT)
                        .description("친구 목록"),
                    fieldWithPath("data.friendList.content[]").type(JsonFieldType.ARRAY)
                        .description("친구 프로필 리스트"),
                    fieldWithPath("data.friendList.content[].friendId").type(JsonFieldType.NUMBER)
                        .description("친구 ID"),
                    fieldWithPath("data.friendList.content[].nickname").type(JsonFieldType.STRING)
                        .description("친구 닉네임"),
                    fieldWithPath("data.friendList.content[].luckPhrase").type(JsonFieldType.STRING)
                        .description("친구의 행운 문구"),
                    fieldWithPath("data.friendList.content[].imageFileUrl").type(JsonFieldType.STRING)
                        .description("친구의 캐릭터 URL"),
                    fieldWithPath("data.friendList.content[].characterCount").type(JsonFieldType.NUMBER)
                        .description("친구의 캐릭터 개수"),
                    fieldWithPath("data.friendList.pageInfo").type(JsonFieldType.OBJECT)
                        .description("페이징 정보"),
                    fieldWithPath("data.friendList.pageInfo.currentPage").type(JsonFieldType.NUMBER)
                        .description("현재 페이지"),
                    fieldWithPath("data.friendList.pageInfo.totalPage").type(JsonFieldType.NUMBER)
                        .description("총 페이지 수"),
                    fieldWithPath("data.friendList.pageInfo.totalElement").type(JsonFieldType.NUMBER)
                        .description("총 요소 개수")
                )));
    }

    @DisplayName("유저들의 1-3위까지 리그 정보를 조회 API")
    @Test
    @WithMockUser("USER")
    void getUserLeagueTop3() throws Exception {
        // given
        UserLeagueResponse res1 = createUserLeagueResponse("테스트1", "https://test.cloudfront.net/캐릭터1.png", 3);
        UserLeagueResponse res2 = createUserLeagueResponse("테스트4", "https://test.cloudfront.net/캐릭터3.png", 3);
        UserLeagueResponse res3 = createUserLeagueResponse("테스트2", "https://test.cloudfront.net/캐릭터4.png", 2);

        given(userReadService.getUserLeagueTop3())
            .willReturn(List.of(res1, res2, res3));

        // when // then
        mockMvc.perform(
                get("/api/v1/garden/league")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("garden-league",
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
                    fieldWithPath("data[].nickname").type(JsonFieldType.STRING)
                        .description("닉네임"),
                    fieldWithPath("data[].imageFileUrl").type(JsonFieldType.STRING)
                        .description("이미지 URL"),
                    fieldWithPath("data[].characterCount").type(JsonFieldType.NUMBER)
                        .description("캐릭터 수")
                )));
    }

    private UserLeagueResponse createUserLeagueResponse(String nickname, String imageFileUrl, int characterCount) {
        return UserLeagueResponse.builder()
            .nickname(nickname)
            .imageFileUrl(imageFileUrl)
            .characterCount(characterCount)
            .build();
    }
}
