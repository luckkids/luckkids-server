package com.luckkids.domain.misson;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "mission", indexes = @Index(name = "IDXAlertStatus", columnList = "alertStatus"))
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private MissionType missionType;

    private String missionDescription;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    private LocalTime alertTime;

    private LocalDateTime deletedDate;

    private LocalDate push_date;

    @Builder
    private Mission(User user, MissionType missionType, String missionDescription, AlertStatus alertStatus, LocalTime alertTime, LocalDateTime deletedDate, LocalDate push_date) {
        this.user = user;
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
        this.deletedDate = deletedDate;
        this.push_date = push_date;
    }

    public Mission update(String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        updateIfNonNull(missionDescription, it -> this.missionDescription = it);
        updateIfNonNull(alertStatus, it -> this.alertStatus = it);
        updateIfNonNull(alertTime, it -> this.alertTime = it);

        return this;
    }

    public void delete(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    private <T> void updateIfNonNull(T value, Consumer<T> consumer) {
        Optional.ofNullable(value).ifPresent(consumer);
    }
}
