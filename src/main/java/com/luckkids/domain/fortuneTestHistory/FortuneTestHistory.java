package com.luckkids.domain.fortuneTestHistory;

import com.luckkids.domain.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FortuneTestHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private FortuneTestResultType resultType;

	@Builder
	private FortuneTestHistory(String nickname, FortuneTestResultType resultType) {
		this.nickname = nickname;
		this.resultType = resultType;
	}
}
