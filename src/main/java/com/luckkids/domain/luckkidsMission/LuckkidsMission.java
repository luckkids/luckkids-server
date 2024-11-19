package com.luckkids.domain.luckkidsMission;

import java.time.LocalTime;

import com.luckkids.domain.BaseTimeEntity;
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
public class LuckkidsMission extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.STRING)
	private MissionType missionType;

	private String missionDescription;

	private LocalTime alertTime;

	private int sort;

	@Builder
	private LuckkidsMission(MissionType missionType, String missionDescription, LocalTime alertTime, int sort) {
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.alertTime = alertTime;
		this.sort = sort;
	}
}
