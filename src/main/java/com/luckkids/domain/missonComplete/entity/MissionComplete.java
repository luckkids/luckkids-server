package com.luckkids.domain.missonComplete.entity;

import java.time.LocalDateTime;

import com.luckkids.domain.user.entity.User;
import com.luckkids.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description 완료한 미션 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class MissionComplete extends BaseEntity{
	/**
	 * 미션완료ID
	 */
	@Id @GeneratedValue
	@Column(name="misson_com_id")
	private Long missionComId;
	/**
	 * 미션완료날짜
	 */
	@Id
	private LocalDateTime missonDate;
	/**
	 * 미션완료유저
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 미션내용
	 */
	private String missonDescription;
	
	//Persist시 당일날짜로 세팅
	@PrePersist
    public void prePersist() {
		missonDate = LocalDateTime.now();
    }
}
