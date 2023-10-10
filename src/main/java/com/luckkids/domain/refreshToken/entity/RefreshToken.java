package com.luckkids.domain.refreshToken.entity;

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
 * @description 리플래쉬토큰 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
	/**
	 * 리플래시토큰 ID
	 */
	@Id @GeneratedValue
	@Column(name = "token_id")
	private Long tokenId;
	/**
	 * 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 리플래시토큰
	 */
	private String refreshToken;
	/**
	 * 다바이스ID
	 */
	private String deviceId;
}
