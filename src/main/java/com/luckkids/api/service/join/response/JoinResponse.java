package com.luckkids.api.service.join.response;

import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JoinResponse {
    private String email;
    private String password;
    private String phoneNumber;
    private SnsType snsType;

    public JoinResponse(User user){
        email = user.getEmail();
        password = user.getPassword();
        phoneNumber = user.getPhoneNumber();
        snsType = user.getSnsType();
    }

    public static JoinResponse of(User user){
        return new JoinResponse(user);
    }
}
