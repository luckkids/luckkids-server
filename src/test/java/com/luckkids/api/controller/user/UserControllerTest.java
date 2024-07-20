package com.luckkids.api.controller.user;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.user.request.UserUpdateLuckPhraseRequest;
import com.luckkids.api.controller.user.request.UserUpdateNicknameRequest;
import com.luckkids.api.controller.user.request.UserUpdatePasswordRequest;

public class UserControllerTest extends ControllerTestSupport {

	@DisplayName("현재 로그인한 사용자 정보를 가져온다.")
	@Test
	@WithMockUser(roles = "USER")
	void findByMe() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/user/me")
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));

	}

	@DisplayName("id값을 받아 사용자 정보를 가져온다.")
	@Test
	@WithMockUser(roles = "USER")
	void findById() throws Exception {
		// given
		int id = 1;

		// when // then
		mockMvc.perform(
				get("/api/v1/user/{id}", id)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));

	}

	@DisplayName("사용자 행운문구를 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateUserLuckPhrase() throws Exception {
		// given
		UserUpdateLuckPhraseRequest request = UserUpdateLuckPhraseRequest.builder()
			.luckPhrase("행운입니다.")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/phrase")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("사용자 행운문구를 수정할시 행운문구는 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateUserLuckPhraseWithoutLuckPhrase() throws Exception {
		// given
		UserUpdateLuckPhraseRequest request = UserUpdateLuckPhraseRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/phrase")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("변경 할 행운문구는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("비밀번호를 재설정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updatePassword() throws Exception {
		// given
		UserUpdatePasswordRequest request = UserUpdatePasswordRequest.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("비밀번호를 재설정시 이메일은 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void updatePasswordWithoutEmail() throws Exception {
		// given
		UserUpdatePasswordRequest request = UserUpdatePasswordRequest.builder()
			.password("1234")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("비밀번호를 재설정시 비밀번호는 필수다.")
	@Test
	@WithMockUser(roles = "USER")
	void updatePasswordWithoutPassword() throws Exception {
		// given
		UserUpdatePasswordRequest request = UserUpdatePasswordRequest.builder()
			.email("test@email.com")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/password")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("패스워드는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("사용자 닉네임을 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateNickname() throws Exception {
		// given
		UserUpdateNicknameRequest request = UserUpdateNicknameRequest.builder()
			.nickname("테스트")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/nickname")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("사용자 닉네임을 수정할시 닉네임은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateNicknameWithoutNickname() throws Exception {
		// given
		UserUpdateNicknameRequest request = UserUpdateNicknameRequest.builder()
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/user/nickname")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("변경 할 닉네임은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("회원탈퇴를 한다.")
	@Test
	@WithMockUser(roles = "USER")
	void userWithDrawTest() throws Exception {

		// when // then
		mockMvc.perform(
				delete("/api/v1/user/withdraw")
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}
}
