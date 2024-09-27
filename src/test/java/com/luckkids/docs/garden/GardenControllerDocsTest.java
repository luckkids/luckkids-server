package com.luckkids.docs.garden;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

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
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.user.projection.MyProfileDto;

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

		MyProfileDto myProfile = new MyProfileDto(1, "럭키즈", "행운문구", CLOVER, 1, 1);

		List<FriendProfileDto> friendProfileList = List.of(
			new FriendProfileDto(2, "럭키즈 친구 2", "행운 문구 2", CLOVER, 1, 120),
			new FriendProfileDto(3, "럭키즈 친구 3", "행운 문구 3", RABBIT, 4, 90),
			new FriendProfileDto(4, "럭키즈 친구 4", "행운 문구 4", SUN, 1, 20)
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
					.param("page", String.valueOf(request.getPage()))
					.param("size", String.valueOf(request.getSize()))
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
					fieldWithPath("data.myProfile.characterType").type(JsonFieldType.STRING)
						.description("내 캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data.myProfile.level").type(JsonFieldType.NUMBER)
						.description("내 캐릭터 레벨"),
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
					fieldWithPath("data.friendList.content[].characterType").type(JsonFieldType.STRING)
						.description("친구의 캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data.friendList.content[].level").type(JsonFieldType.NUMBER)
						.description("친구의 캐릭터 레벨"),
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
		UserLeagueResponse res1 = createUserLeagueResponse("테스트1", CLOVER, 1, 3);
		UserLeagueResponse res2 = createUserLeagueResponse("테스트4", CLOUD, 3, 3);
		UserLeagueResponse res3 = createUserLeagueResponse("테스트2", STONE, 2, 2);

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
						.description("닉네임 (null 가능)"),
					fieldWithPath("data[].characterType").type(JsonFieldType.STRING)
						.description("캐릭터 타입, 가능한값: " + Arrays.toString(CharacterType.values())),
					fieldWithPath("data[].level").type(JsonFieldType.NUMBER)
						.description("캐릭터 레벨"),
					fieldWithPath("data[].characterCount").type(JsonFieldType.NUMBER)
						.description("캐릭터 수")
				)));
	}

	private UserLeagueResponse createUserLeagueResponse(String nickname, CharacterType characterType, int level,
		int characterCount) {
		return UserLeagueResponse.builder()
			.nickname(nickname)
			.characterType(characterType)
			.level(level)
			.characterCount(characterCount)
			.build();
	}
}
