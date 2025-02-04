package com.luckkids.domain.luckMessageHistory;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.notification.domain.push.Push;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class LuckMessageHistory extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Push push;

	private String messageDescription;

	@Builder
	public LuckMessageHistory(int id, Push push, String messageDescription) {
		this.id = id;
		this.push = push;
		this.messageDescription = messageDescription;
	}
}
