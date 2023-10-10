package com.luckkids.domain.alertHistory.entity;

import com.luckkids.domain.user.entity.User;
import com.luckkids.global.entity.YesNoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description 알림이력 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class AlertHistory {
	/**
	 * 이력ID
	 */
	@Id @GeneratedValue
	@Column(name = "alert_id")
	private Long alertId;
	/**
	 * 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 알림내용
	 */
	private String alertDescription;
	/**
	 * 확인여부
	 */
	@Enumerated(EnumType.STRING)
	private YesNoEnum completeYn;
}
