package com.luckkids.join;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.join.dto.JoinResponse;
import com.luckkids.api.service.join.dto.JoinSendMailServiceRequest;
import com.luckkids.api.service.join.dto.JoinServiceRequest;
import com.luckkids.domain.user.SnsType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class JoinServiceTest {

    @Autowired
    private JoinService joinService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendMail() throws Exception{
        //given
        JoinSendMailServiceRequest request = JoinSendMailServiceRequest.builder()
                .email("tkdrl8908@naver.com")
                .build();

//        JoinSendMailServiceResponse response = JoinSendMailServiceResponse.builder()
//                .code("001")
//                .build();

//        given(joinService.sendMail(any(JoinSendMailServiceRequest.class)))

        // when // then
        mockMvc.perform(
                        post("/api/v1/join/sendMail")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void saveUser() throws  Exception{
        JoinServiceRequest joinRequest = JoinServiceRequest.builder()
                .email("tkdrl8908@naver.com")
                .nickname("상기")
                .password("1234")
                .phoneNumber("01064091048")
                .build();

        JoinResponse joinResponse = JoinResponse.builder()
                .email("tkdrl8908@naver.com")
                .nickname("상기")
                .password("1234")
                .phoneNumber("01064091048")
                .snsType(SnsType.NORMAL.getText())
                .build();

        given(joinService.joinUser(any(JoinServiceRequest.class)));

        mockMvc.perform(
                        post("/api/v1/join/user")
                                .content(objectMapper.writeValueAsString(joinRequest))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }
}
