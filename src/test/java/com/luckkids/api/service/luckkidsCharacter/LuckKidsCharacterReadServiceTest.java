package com.luckkids.api.service.luckkidsCharacter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.luckkidsCharacter.response.LuckkidsCharacterRandResponse;
import com.luckkids.domain.luckkidsCharacter.CharacterType;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;

public class LuckKidsCharacterReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private LuckkidsCharacterReadService luckkidsCharacterReadService;

	@Autowired
	private LuckkidsCharacterRepository luckkidsCharacterRepository;

	@AfterEach
	void tearDown() {
		luckkidsCharacterRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("초기캐릭터를 랜덤 조회한다.")
	void findRandomCharacterLevel1() {
		//given
		settingCharacter();
		//when
		LuckkidsCharacterRandResponse luckkidsCharacterRandResponse = luckkidsCharacterReadService.findRandomCharacterLevel1();

		//then
		assertThat(luckkidsCharacterRandResponse.getCharacterType()).isNotNull();
		assertThat(luckkidsCharacterRandResponse.getLevel()).isEqualTo(1);
	}

	@Test
	@DisplayName("초기캐릭터 Entity를 랜덤 조회한다.")
	void findRandomLuckkidsCharacterLevel1() {
		//given
		settingCharacter();
		//when
		LuckkidsCharacter luckkidsCharacter = luckkidsCharacterReadService.findRandomLuckkidsCharacterLevel1();

		//then
		assertThat(luckkidsCharacter.getCharacterType()).isNotNull();
		assertThat(luckkidsCharacter.getLevel()).isEqualTo(1);
	}

	@Test
	@DisplayName("초기캐릭터 Entity 랜덤 조회 시 캐릭터가 없을 시 예외가 발생한다.")
	void findRandomLuckkidsCharacterLevel1ThrowException() {
		//when
		//then
		assertThatThrownBy(() -> luckkidsCharacterReadService.findRandomLuckkidsCharacterLevel1())
			.hasMessage("럭키즈 캐릭터가 존재하지않습니다.")
			.isInstanceOf(LuckKidsException.class);
	}

	@Test
	@DisplayName("초기캐릭터 Entity를 id로 조회한다.")
	void findById() {
		//given
		LuckkidsCharacter luckkidsCharacter = createCharacter(CharacterType.SUN, 1);
		luckkidsCharacterRepository.save(luckkidsCharacter);

		//when
		LuckkidsCharacter findLuckKidsCharacter = luckkidsCharacterReadService.findById(luckkidsCharacter.getId());

		//then
		assertThat(findLuckKidsCharacter).extracting("id", "characterType", "level")
			.contains(luckkidsCharacter.getId(), luckkidsCharacter.getCharacterType(), luckkidsCharacter.getLevel());
	}

	@Test
	@DisplayName("초기캐릭터 Entity를 id로 조회시 데이터가 존재하지 않으면 예외가 발생한다.")
	void findByIdThrowException() {
		//when
		//then
		assertThatThrownBy(() -> luckkidsCharacterReadService.findById(1))
			.hasMessage("럭키즈 캐릭터가 존재하지않습니다.")
			.isInstanceOf(LuckKidsException.class);
	}

	private LuckkidsCharacter createCharacter(CharacterType characterType, int level) {
		return LuckkidsCharacter.builder()
			.characterType(characterType)
			.level(level)
			.build();
	}

	private void settingCharacter() {
		CharacterType[] characterTypes = CharacterType.values();

		for (CharacterType characterType : characterTypes) {
			luckkidsCharacterRepository.save(createCharacter(characterType, 1));
		}

		for (CharacterType characterType : characterTypes) {
			luckkidsCharacterRepository.save(createCharacter(characterType, 2));
		}
	}
}
