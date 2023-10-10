package com.luckkids.domain.push.entity;

import com.luckkids.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description 푸시 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class Push {
	/**
	 * 푸시토큰 ID
	 */
	@Id @GeneratedValue
	@Column(name = "push_id")
	private Long pushId;
	/**
	 * 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 푸시토큰
	 */
	private String pushToken;
	/**
	 * 디바이스ID -> 디바이스ID가 없으면 푸시토큰을 요청 받았을 시 어떤 디바이스를 UPDATE할 지 모호해짐
	 */
	private String deviceId;
}
