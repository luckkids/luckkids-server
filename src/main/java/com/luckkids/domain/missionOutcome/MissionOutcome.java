package com.luckkids.domain.missionOutcome;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"mission_id", "missionDate"}))
public class MissionOutcome extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    private LocalDate missionDate;

    @Builder
    private MissionOutcome(Mission mission, MissionStatus missionStatus, LocalDate missionDate) {
        this.mission = mission;
        this.missionStatus = missionStatus;
        this.missionDate = missionDate;
    }

    public MissionOutcome updateOf(MissionStatus missionStatus) {
        this.missionStatus = missionStatus;

        return this;
    }
}
