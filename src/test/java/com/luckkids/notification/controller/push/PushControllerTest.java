package com.luckkids.notification.controller.push;

import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.ControllerTestSupport;
import com.luckkids.notification.controller.request.PushSoundChangeRequest;

public class PushControllerTest extends ControllerTestSupport {

	@DisplayName("푸시 사운드를 수정한다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateSound() throws Exception {
		// given
		PushSoundChangeRequest request = PushSoundChangeRequest.builder()
			.deviceId("DeviceId")
			.sound("sound.wav")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/push")
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

	@DisplayName("푸시 사운드를 수정할 시 DeviceId는 필수이다.")
	@Test
	@WithMockUser(roles = "USER")
	void updateSoundWithoutDeviceId() throws Exception {
		// given
		PushSoundChangeRequest request = PushSoundChangeRequest.builder()
			.sound("sound.wav")
			.build();

		// when // then
		mockMvc.perform(
				patch("/api/v1/push")
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
					.with(csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.statusCode").value("400"))
			.andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("DeviceId는 필수입니다."));
	}
}
