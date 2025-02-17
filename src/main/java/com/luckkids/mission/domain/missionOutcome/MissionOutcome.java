package com.luckkids.mission.domain.missionOutcome;

import static com.luckkids.mission.domain.missionOutcome.SuccessChecked.*;

import java.time.LocalDate;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.mission.domain.misson.Mission;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private MissionOutcome(Mission mission, MissionStatus missionStatus, SuccessChecked successChecked,
		LocalDate missionDate) {
		this.mission = mission;
		this.missionStatus = missionStatus;
		this.successChecked = successChecked;
		this.missionDate = missionDate;
	}

	public void updateMissionStatus(MissionStatus missionStatus) {
		this.missionStatus = missionStatus;
	}

	public void updateFirstSuccessChecked() {
		successChecked = FIRST_CHECKED;
	}
}
