package com.luckkids.docs.user;

import static com.luckkids.domain.luckkidsCharacter.CharacterType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import com.luckkids.api.controller.user.UserController;
import com.luckkids.api.controller.user.request.UserFindEmailRequest;
import com.luckkids.api.controller.user.request.UserUpdateLuckPhraseRequest;
import com.luckkids.api.controller.user.request.UserUpdateNicknameRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.user.request.UserFindEmailServiceRequest;
import com.luckkids.api.service.user.request.UserUpdateLuckPhraseServiceRequest;
import com.luckkids.api.service.user.request.UserUpdateNicknameServiceRequest;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserFindSnsTypeResponse;
import com.luckkids.api.service.user.response.UserResponse;
import com.luckkids.api.service.user.response.UserUpdateLuckPhraseResponse;
import com.luckkids.api.service.user.response.UserUpdateNicknameResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.api.service.user.response.UserWithdrawResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.projection.InProgressCharacterDto;

public class UserControllerDocsTest extends RestDocsSupport {

	private final UserService userService = mock(UserService.class);
	private final UserReadService userReadService = mock(UserReadService.class);

	@Override
	protected Object initController() {
		return new UserController(userService, userReadService);
	}

	@DisplayName("현재 로그인한 사용자 정보 조회 API")
	@Test
	void findByMe() throws Exception {
		// given
		given(userReadService.findByMe())
			.willReturn(
				UserResponse.builder()
					.email("test@gmail.com")
					.nickname("test")
					.snsType(SnsType.GOOGLE)
					.luckPhrase("테스트 행운문구입니다.")
					.role(Role.USER)
					.settingStatus(SettingStatus.COMPLETE)
					.missionCount(0)
					.inProgressCharacter(
						new InProgressCharacterDto(CLOVER, 1)
					)
					.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/user/me")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("get-me",
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
					fieldWithPath("data.email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("data.snsType").type(JsonFieldType.STRING)
						.description("SNS 타입"),
					fieldWithPath("data.luckPhrase").type(JsonFieldType.STRING)
						.description("행운문구"),
					fieldWithPath("data.role").type(JsonFieldType.STRING)
						.description("역할"),
					fieldWithPath("data.settingStatus").type(JsonFieldType.STRING)
						.description("초기세팅상태"),
					fieldWithPath("data.missionCount").type(JsonFieldType.NUMBER)
						.description("미션성공갯수"),
					fieldWithPath("data.inProgressCharacter.characterType").type(JsonFieldType.STRING)
						.description("진행중인 캐릭터 타입"),
					fieldWithPath("data.inProgressCharacter.level").type(JsonFieldType.NUMBER)
						.description("진행중인 캐릭터 레벨")
				)
			));
	}

	@DisplayName("id값을 받아 사용자 정보 조회 API")
	@Test
	void findById() throws Exception {
		// given
		int userId = 1;

		given(userReadService.findById(userId))
			.willReturn(
				UserResponse.builder()
					.email("test@gmail.com")
					.nickname("test")
					.snsType(SnsType.GOOGLE)
					.luckPhrase("테스트 행운문구입니다.")
					.role(Role.USER)
					.settingStatus(SettingStatus.COMPLETE)
					.missionCount(0)
					.inProgressCharacter(
						new InProgressCharacterDto(CLOVER, 1)
					)
					.build()
			);

		// when // then
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/v1/user/{id}", userId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("get-user",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("id")
						.description("유저의 id 값")
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
					fieldWithPath("data.email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("data.snsType").type(JsonFieldType.STRING)
						.description("SNS 타입"),
					fieldWithPath("data.luckPhrase").type(JsonFieldType.STRING)
						.description("행운문구"),
					fieldWithPath("data.role").type(JsonFieldType.STRING)
						.description("역할"),
					fieldWithPath("data.settingStatus").type(JsonFieldType.STRING)
						.description("초기세팅상태"),
					fieldWithPath("data.missionCount").type(JsonFieldType.NUMBER)
						.description("미션성공갯수"),
					fieldWithPath("data.inProgressCharacter.characterType").type(JsonFieldType.STRING)
						.description("진행중인 캐릭터 타입"),
					fieldWithPath("data.inProgressCharacter.level").type(JsonFieldType.NUMBER)
						.description("진행중인 캐릭터 레벨")
				)
			));
	}

	@DisplayName("사용자 프로필 행운문구 수정 API")
	@Test
	void updatePhrase() throws Exception {
		// given
		UserUpdateLuckPhraseRequest request = UserUpdateLuckPhraseRequest.builder()
			.luckPhrase("행운입니다.!")
			.build();

		given(userService.updatePhrase(any(UserUpdateLuckPhraseServiceRequest.class)))
			.willReturn(UserUpdateLuckPhraseResponse.builder()
				.luckPhrase("행운입니다.!")
				.build()
			);

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/phrase")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("update-phrase",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("luckPhrase").type(JsonFieldType.STRING)
						.description("행운문구")
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
					fieldWithPath("data.luckPhrase").type(JsonFieldType.STRING)
						.description("행운문구")
				)
			));
	}

	@DisplayName("비밀번호 재설정 API")
	@Test
	void updatePassword() throws Exception {
		// given
		UserUpdatePasswordRequest request = UserUpdatePasswordRequest.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		given(userService.updatePassword(any(UserUpdatePasswordServiceRequest.class)))
			.willReturn(
				UserUpdatePasswordResponse.builder()
					.email("test@email.com")
					.build()
			);

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/password/")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("update-password",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호")
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
					fieldWithPath("data.email").type(JsonFieldType.STRING)
						.description("이메일")
				)
			));
	}

	@DisplayName("사용자 프로필 닉네임 수정 API")
	@Test
	void updateNickname() throws Exception {
		// given
		String nickname = "테스트";

		UserUpdateNicknameRequest request = UserUpdateNicknameRequest.builder()
			.nickname(nickname)
			.build();

		given(userService.updateNickname(any(UserUpdateNicknameServiceRequest.class)))
			.willReturn(UserUpdateNicknameResponse.of(nickname));

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/nickname")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("update-nickname",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임")
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
					fieldWithPath("data.nickname").type(JsonFieldType.STRING)
						.description("닉네임")
				)
			));
	}

	@DisplayName("이메일 SNS타입 확인 API")
	@Test
	void findSnsType() throws Exception {
		// given
		UserFindEmailRequest request = UserFindEmailRequest.builder()
			.email("test@email.com")
			.build();

		given(userReadService.findEmail(any(UserFindEmailServiceRequest.class)))
			.willReturn(
				UserFindSnsTypeResponse.builder()
					.snsType(SnsType.NORMAL)
					.build()
			);

		// when // then
		mockMvc.perform(
				get("/api/v1/user/findEmail")
					.param("email", request.getEmail())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-findEmail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("email")
						.description("이메일")
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
					fieldWithPath("data.snsType").type(JsonFieldType.STRING)
						.description("가입한 SNS형식. 가능한 값: " + Arrays.toString(SnsType.values()))
				)
			));
	}

	@DisplayName("회원탈퇴 API")
	@Test
	void withdraw() throws Exception {
		given(userService.withdraw())
			.willReturn(
				UserWithdrawResponse.builder()
					.id(12)
					.build()
			);
		// when // then
		mockMvc.perform(
				delete("/api/v1/user/withdraw")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("user-withdraw",
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
						.description("탈퇴완료된 id")
				)
			));
	}
}
