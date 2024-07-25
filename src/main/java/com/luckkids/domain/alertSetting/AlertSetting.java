package com.luckkids.domain.alertSetting;

import java.time.LocalTime;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.misson.AlertStatus;
import com.luckkids.domain.push.Push;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AlertSetting extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Push push;

	@Enumerated(EnumType.STRING)
	private AlertStatus entire;

	@Enumerated(EnumType.STRING)
	private AlertStatus mission;

	@Enumerated(EnumType.STRING)
	private AlertStatus luckMessage;

	private LocalTime luckMessageAlertTime;

	@Enumerated(EnumType.STRING)
	private AlertStatus notice;

	@Builder
	private AlertSetting(Push push, AlertStatus entire, AlertStatus mission, AlertStatus luckMessage,
		LocalTime luckMessageAlertTime, AlertStatus notice) {
		this.push = push;
		this.entire = entire;
		this.mission = mission;
		this.luckMessage = luckMessage;
		this.luckMessageAlertTime = luckMessageAlertTime;
		this.notice = notice;
	}

	public void update(AlertType alertType, AlertStatus alertStatus) {
		switch (alertType) {
			case ENTIRE -> this.entire = alertStatus;
			case MISSION -> this.mission = alertStatus;
			case LUCK -> this.luckMessage = alertStatus;
			case NOTICE -> this.notice = alertStatus;
		}
	}

	public static AlertSetting of(Push push, AlertStatus alertStatus) {
		return AlertSetting.builder()
			.push(push)
			.entire(alertStatus)
			.mission(alertStatus)
			.luckMessage(alertStatus)
			.notice(alertStatus)
			.build();
	}
}
