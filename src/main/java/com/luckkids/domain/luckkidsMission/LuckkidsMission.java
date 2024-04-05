package com.luckkids.domain.luckkidsMission;

import java.time.LocalTime;

import com.luckkids.domain.misson.MissionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private LuckkidsMission(int id, MissionType missionType, String missionDescription, LocalTime alertTime) {
		this.id = id;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertTime = alertTime;
	}
}
