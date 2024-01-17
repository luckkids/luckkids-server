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

    @Builder
    private JoinServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
            .email(email)
            .password(encryptPassword())
            .missionCount(0)
            .snsType(SnsType.NORMAL)
            .role(Role.USER)
            .settingStatus(SettingStatus.INCOMPLETE)
            .build();
    }

    public String encryptPassword(){
        return password;
    }
}
