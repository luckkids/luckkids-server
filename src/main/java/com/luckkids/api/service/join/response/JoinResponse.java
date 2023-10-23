package com.luckkids.api.service.join.response;

import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinResponse {

    private int id;
    private String email;
    private String phoneNumber;
    private SnsType snsType;

    @Builder
    private JoinResponse(int id, String email, String phoneNumber, SnsType snsType){
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.snsType = snsType;
    }

    public static JoinResponse of(User user){
        return JoinResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .snsType(user.getSnsType())
            .build();
    }
}
