package com.luckkids.domain.luckkidsCharacter;

import java.util.List;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.userCharacter.UserCharacter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"character_type", "level"}))
public class LuckkidsCharacter extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "character_type")
	private CharacterType characterType;

	private int level;

	@OneToMany(mappedBy = "luckkidsCharacter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserCharacter> userCharacter;

	@Builder
	public LuckkidsCharacter(CharacterType characterType, int level) {
		this.characterType = characterType;
		this.level = level;
	}
}