package com.luckkids.domain.confirmEmail;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class ConfirmEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String authKey;
    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    @Builder
    private ConfirmEmail(String email, String authKey, ConfirmStatus confirmStatus) {
        this.email = email;
        this.authKey = authKey;
        this.confirmStatus = confirmStatus;
    }

    public void confirm(){
        this.confirmStatus = ConfirmStatus.COMPLETE;
    }

    public void checkEmail(){
        Optional.of(confirmStatus)
            .filter(status -> status.equals(ConfirmStatus.COMPLETE))
            .orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_INCOMPLETE));
    }
}
