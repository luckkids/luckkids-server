package com.luckkids.domain.misson;

import com.luckkids.domain.BaseEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.luckkids.domain.misson.MissionStatus.AWAITING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String missionDescription;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    private LocalTime alertTime;

    private LocalDate missionDate;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @Builder
    private Mission(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime, MissionStatus missionStatus) {
        this.user = user;
        this.missionDescription = missionDescription;
        this.alertStatus = alertStatus;
        this.alertTime = alertTime;
        this.missionStatus = missionStatus;
    }

    public static Mission create(User user, String missionDescription, AlertStatus alertStatus, LocalTime alertTime) {
        return Mission.builder()
            .user(user)
            .missionDescription(missionDescription)
            .alertStatus(alertStatus)
            .alertTime(alertTime)
            .missionStatus(AWAITING)
            .build();
    }
}
