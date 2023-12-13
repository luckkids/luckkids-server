package com.luckkids.domain.confirmEmail;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class ConfirmEmail extends BaseTimeEntity {
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
        checkTime();
        this.confirmStatus = ConfirmStatus.COMPLETE;
    }

    public void checkEmail(){
        checkTime();
        Optional.of(confirmStatus)
            .filter(status -> status.equals(ConfirmStatus.COMPLETE))
            .orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_INCOMPLETE));
    }

    private void checkTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(getCreatedDate(), currentTime);
        if (duration.toMinutes() > 3) {
            throw new LuckKidsException(ErrorCode.EMAIL_EXPIRED);
        }
    }
}
