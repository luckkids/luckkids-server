package com.luckkids.api.controller.friend;

import com.luckkids.ControllerTestSupport;
import com.luckkids.api.controller.request.PageInfoRequest;
import com.luckkids.jwt.dto.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FriendControllerTest extends ControllerTestSupport {

    @DisplayName("친구 랭킹 리스트 조회")
    @Test
    @WithMockUser("USER")
    void readList() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
            .size(10)
            .page(1)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/list")
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

    @DisplayName("친구 랭킹 리스트 조회시 페이지사이즈는 1이상이여야한다.")
    @Test
    @WithMockUser("USER")
    void readListWithoutSize() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
            .page(1)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/list")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("페이지사이즈는 1이상부터 입력가능합니다."));
    }

    @DisplayName("친구 랭킹 리스트 조회 페이지는 1이상이여야한다.")
    @Test
    @WithMockUser("USER")
    void readListWithoutPage() throws Exception {
        // given
        PageInfoRequest request = PageInfoRequest.builder()
            .size(10)
            .build();

        given(securityService.getCurrentUserInfo())
            .willReturn(createUserInfo());

        // when // then
        mockMvc.perform(
                get("/api/v1/friend/list")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value("400"))
            .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("페이지는 1이상부터 입력가능합니다."));
    }

    @DisplayName("친구 프로필 조회를 한다.")
    @Test
    @WithMockUser("USER")
    void readProfile() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                get("/api/v1/friend/profile/{friendId}",1)
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

    private UserInfo createUserInfo() {
        return UserInfo.builder()
            .userId(1)
            .email("")
            .build();
    }

}
