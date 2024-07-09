package com.luckkids.domain.misson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Mission extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private Integer luckkidsMissionId;

	@Enumerated(EnumType.STRING)
	private MissionType missionType;

	private String missionDescription;

	private MissionActive missionActive;

	@Enumerated(EnumType.STRING)
	private AlertStatus alertStatus;

	private LocalTime alertTime;

	private LocalDate push_date;

	private LocalDateTime deletedDate;

	@Builder
	private Mission(User user, Integer luckkidsMissionId, MissionType missionType, String missionDescription,
		MissionActive missionActive, AlertStatus alertStatus, LocalTime alertTime, LocalDate push_date,
		LocalDateTime deletedDate) {
		this.user = user;
		this.luckkidsMissionId = luckkidsMissionId;
		this.missionType = missionType;
		this.missionDescription = missionDescription;
		this.missionActive = missionActive;
		this.alertStatus = alertStatus;
		this.alertTime = alertTime;
		this.push_date = push_date;
		this.deletedDate = deletedDate;
	}

	public Mission update(MissionType missionType, String missionDescription, MissionActive missionActive,
		AlertStatus alertStatus, LocalTime alertTime) {
		updateIfNonNull(missionType, it -> this.missionType = it);
		updateIfNonNull(missionDescription, it -> this.missionDescription = it);
		updateIfNonNull(missionActive, it -> this.missionActive = it);
		updateIfNonNull(alertStatus, it -> this.alertStatus = it);
		updateIfNonNull(alertTime, it -> this.alertTime = it);

		return this;
	}

	public void delete(LocalDateTime deletedDate) {
		this.deletedDate = deletedDate;
	}

	private <T> void updateIfNonNull(T value, Consumer<T> consumer) {
		Optional.ofNullable(value).ifPresent(consumer);
	}
}
