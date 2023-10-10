package com.luckkids.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.luckkids.domain.alertHistory.entity.AlertHistory;
import com.luckkids.domain.friends.entity.Friend;
import com.luckkids.domain.misson.entity.Mission;
import com.luckkids.domain.missonComplete.entity.MissionComplete;
import com.luckkids.domain.push.entity.Push;
import com.luckkids.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description 사용자 테이블 Entity
 * @author skhan
 */
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity{
	/**
	 * 사용자 ID
	 */
	@Id
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 이메일
	 */
	private String email;
	/**
	 * 비밀번호 snsType이 normal일시만 필요
	 */
	private String password;
	/**
	 * 로그인타입
	 * NORMAL, KAKAO, GOOGLE, APPLE
	 */
	@Enumerated(EnumType.STRING)
	private SnsType snsType;
	/**
	 * 사용자 핸드폰번호
	 */
	private String phone;
	/**
	 * 사용자 등록미션
	 */
	@OneToMany(mappedBy = "user")
	private List<Mission> missions = new ArrayList<>();
	/**
	 * 사용자 완료미션
	 */
	@OneToMany(mappedBy = "user")
	private List<MissionComplete> missionCompletes = new ArrayList<>();
	/**
	 * 사용자가 등록한 친구목록
	 */
	@OneToMany(mappedBy = "userId1")
    private List<Friend> friend1 = new ArrayList<>();;
	/**
	 * 사용자를 등록한 친구목록
	 */
    @OneToMany(mappedBy = "userId2")
    private List<Friend> friend2 = new ArrayList<>();;
    /**
	 * 사용자 알림이력
	 */
    @OneToMany(mappedBy = "user")
    private List<AlertHistory> alertHistorys = new ArrayList<>();;
    /**
	 * 사용자 푸시토큰
	 */
    @OneToMany(mappedBy = "user")
    private List<Push> pushTokens = new ArrayList<>();;
}
