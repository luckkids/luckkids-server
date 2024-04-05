package com.luckkids.domain.misson.projection;

import java.time.LocalTime;

import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.misson.MissionType;

public record LuckkidsUserMissionDto(Integer luckkidsMissionId, Integer missionId, MissionType missionType,
									 String missionDescription, LocalTime alertTime, AlertStatus alertStatus,
									 Boolean isLuckkidsMission, Boolean isSelected) {
}
