package com.luckkids.domain.misson;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    private String missionDescription;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    private LocalTime alertTime;

    @Builder
    private Mission(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        this.user = user;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
    }

    public Mission update(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        updateIfNonNull(missionDescription, it -> this.missionDescription = it);
        updateIfNonNull(alertStatus, it -> this.alertStatus = it);
        updateIfNonNull(alertTime, it -> this.alertTime = it);

        return this;
    }

    private <T> void updateIfNonNull(T value, Consumer<T> consumer) {
        Optional.ofNullable(value).ifPresent(consumer);
    }
}
