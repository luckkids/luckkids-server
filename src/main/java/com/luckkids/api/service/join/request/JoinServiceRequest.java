package com.luckkids.api.service.join.request;

import com.luckkids.domain.user.Role;
import com.luckkids.domain.user.SettingStatus;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinServiceRequest {

    private String email;
    private String password;
    private String phoneNumber;

    @Builder
    private JoinServiceRequest(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity(){
        return User.builder()
            .email(email)
            .password(password)
            .phoneNumber(phoneNumber)
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();
    }
}
