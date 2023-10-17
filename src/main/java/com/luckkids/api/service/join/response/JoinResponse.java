package com.luckkids.api.service.join.response;

import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JoinResponse {

    private int id;
    private String email;
    private String password;
    private String phoneNumber;
    private SnsType snsType;
    private Role role;

    @Builder
    public JoinResponse(User user){
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        phoneNumber = user.getPhoneNumber();
        snsType = user.getSnsType();
        role = user.getRole();
    }

    public static JoinResponse of(User user){
        return JoinResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .password(user.getPassword())
            .phoneNumber(user.getPhoneNumber())
            .snsType(user.getSnsType())
            .role(user.getRole())
            .build();
    }
}
