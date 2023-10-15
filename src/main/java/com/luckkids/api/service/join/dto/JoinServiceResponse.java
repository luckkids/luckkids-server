package com.luckkids.api.service.join.dto;

import com.luckkids.api.controller.join.dto.JoinResponse;
import com.luckkids.domain.user.User;
import lombok.Getter;
import org.hibernate.mapping.Join;

@Getter
public class JoinServiceResponse {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String snsType;

    public JoinServiceResponse(User user){
        email = user.getEmail();
        password = user.getPassword();
        nickname = user.getNickName();
        phoneNumber = user.getPhoneNumber();
        snsType = user.getSnsType().getText();
    }

    public static JoinServiceResponse of(User user){
        return new JoinServiceResponse(user);
    }

    public JoinResponse toControllerResponse(){
        return JoinResponse.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .snsType(snsType)
                .build();
    }
}
