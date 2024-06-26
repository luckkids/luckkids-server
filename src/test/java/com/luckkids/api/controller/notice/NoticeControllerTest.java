package com.luckkids.api.controller.notice;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.notice.request.NoticeSaveRequest;

public class NoticeControllerTest extends ControllerTestSupport {

	@DisplayName("공지사항 목록을 조회한다.")
	@Test
	@WithMockUser(roles = "USER")
	void selectNoticeList() throws Exception {
		// given

		// when // then
		mockMvc.perform(
				get("/api/v1/notices")
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.statusCode").value("200"))
			.andExpect(jsonPath("$.httpStatus").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("공지사항을 저장한다.")
	@Test
	@WithMockUser(roles = "USER")
	void saveNotice() throws Exception {
		// given
		NoticeSaveRequest noticeSaveRequest = NoticeSaveRequest.builder()
			.title("공지사항 타이틀")
			.url("www.naver.com")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/notices/new")
					.content(objectMapper.writeValueAsString(noticeSaveRequest))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.statusCode").value("201"))
			.andExpect(jsonPath("$.httpStatus").value("CREATED"))
			.andExpect(jsonPath("$.message").value("CREATED"));
	}

	@DisplayName("공지사항을 저장할시 공지사항 url 은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void saveNoticeWithoutDescription() throws Exception {
		// given
		NoticeSaveRequest noticeSaveRequest = NoticeSaveRequest.builder()
			.title("공지사항 타이틀")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/notices/new")
					.content(objectMapper.writeValueAsString(noticeSaveRequest))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("공지사항 URL은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@DisplayName("공지사항을 저장할시 공지사항 타이틀은 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void saveNoticeWithoutTitle() throws Exception {
		// given
		NoticeSaveRequest noticeSaveRequest = NoticeSaveRequest.builder()
			.url("www.naver.com")
			.build();

		// when // then
		mockMvc.perform(
				post("/api/v1/notices/new")
					.content(objectMapper.writeValueAsString(noticeSaveRequest))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("공지사항 제목은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}
}
