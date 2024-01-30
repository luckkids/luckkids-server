package com.luckkids.domain.missionOutcome;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.misson.Mission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.luckkids.domain.missionOutcome.SuccessChecked.FIRST_CHECKED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"mission_id", "missionDate"}))
public class MissionOutcome extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @Enumerated(EnumType.STRING)
    private SuccessChecked successChecked;

    private LocalDate missionDate;

    @Builder
    private MissionOutcome(Mission mission, MissionStatus missionStatus, SuccessChecked successChecked, LocalDate missionDate) {
        this.mission = mission;
        this.missionStatus = missionStatus;
        this.successChecked = successChecked;
        this.missionDate = missionDate;
    }

    public void updateMissionStatus(MissionStatus missionStatus) {
        this.missionStatus = missionStatus;
    }

    public void updateSuccessChecked() {
        successChecked = FIRST_CHECKED;
    }
}
