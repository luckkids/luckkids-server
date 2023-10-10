package com.luckkids.domain.misson.entity;

import java.time.LocalDateTime;

import com.luckkids.domain.user.entity.User;
import com.luckkids.global.entity.BaseEntity;
import com.luckkids.global.entity.YesNoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description 미션 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class Mission extends BaseEntity{
	/**
	 * 미션ID
	 */
	@Id @GeneratedValue
	@Column(name="misson_id")
	private Long missionId;
	/**
	 * 미션등록유저
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 미션내용
	 */
	private String missonDescription;
	/**
	 * 알림설정
	 */
	private YesNoEnum alertYn;
	/**
	 * 알림시간
	 */
	private LocalDateTime alertDate;
	/**
	 * 미션완료여부
	 */
	@Enumerated(EnumType.STRING)
	private YesNoEnum missonYn;
	
}
