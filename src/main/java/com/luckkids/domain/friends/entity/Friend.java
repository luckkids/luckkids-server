package com.luckkids.domain.friends.entity;

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
 * @description 친구 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class Friend {
	/**
	 * 친구ID
	 */
	@Id @GeneratedValue
	@Column(name = "friend_id")
	private Long friendId;
	/**
	 * 친구요청을 한 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id1", referencedColumnName = "user_id")
	private User userId1;
	/**
	 * 친구요청을 받은 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id2", referencedColumnName = "user_id")
	private User userId2;
	/**
	 * 친구수락여부
	 */
	@Enumerated(EnumType.STRING)
	private YesNoEnum friendYn;
}
