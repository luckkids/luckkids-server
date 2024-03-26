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

    private String description;

    private LocalTime alertTime;

    @Builder
    private LuckkidsMission(MissionType missionType, String description, LocalTime alertTime) {
        this.missionType = missionType;
        this.description = description;
        this.alertTime = alertTime;
    }
}
