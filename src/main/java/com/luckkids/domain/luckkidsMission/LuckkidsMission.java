package com.luckkids.domain.luckkidsMission;

import com.luckkids.domain.misson.MissionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LuckkidsMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private MissionType missionType;

    private String missionDescription;

    private LocalTime alertTime;

    @Builder
    private LuckkidsMission(MissionType missionType, String missionDescription, LocalTime alertTime) {
        this.missionType = missionType;
        this.missionDescription = missionDescription;
        this.alertTime = alertTime;
    }
}
