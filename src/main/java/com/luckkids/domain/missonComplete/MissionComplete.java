package com.luckkids.domain.missonComplete;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.misson.Mission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MissionComplete extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    private LocalDate missionDate;

    @Builder
    private MissionComplete(Mission mission, MissionStatus missionStatus, LocalDate missionDate) {
        this.mission = mission;
        this.missionStatus = missionStatus;
        this.missionDate = missionDate;
    }
}
