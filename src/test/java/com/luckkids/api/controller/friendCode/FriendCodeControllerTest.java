package com.luckkids.api.controller.friendCode;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.friendCode.request.FriendCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendCodeControllerTest extends ControllerTestSupport {

    @DisplayName("친구코드를 생성한다.")
    @Test
    @WithMockUser("USER")
    void createCode() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                get("/api/v1/friendcode")
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구코드로 친구의 닉네임을 조회한다.")
    @Test
    @WithMockUser("USER")
    void findNickNameByCodeTest() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                        get("/api/v1/friendcode/{code}/nickname", "ASDWEDSS")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구코드로 친구를 추가한다.")
    @Test
    @WithMockUser("USER")
    void createFriend() throws Exception {
        // given
        FriendCreateRequest request = FriendCreateRequest.builder()
            .code("ASDWEDSS")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/friendcode/create")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode").value("200"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구코드로 친구를 추가할 시 친구코드는 필수이다.")
    @Test
    @WithMockUser("USER")
    void createFriendWithoutCode() throws Exception {
        // given
        FriendCreateRequest request = FriendCreateRequest.builder()
            .build();

        // when // then
        mockMvc.perform(
                post("/api/v1/friendcode/create")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("친구코드는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty());
    }
}

