package com.luckkids.api.service.luckkidsCharacter;

import org.springframework.stereotype.Service;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.luckkidsCharacter.response.LuckCharacterRandResponse;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LuckkidsCharacterReadService {

	private final LuckkidsCharacterRepository luckkidsCharacterRepository;

	public LuckkidsCharacter findById(int id) {
		return luckkidsCharacterRepository.findById(id)
			.orElseThrow(() -> new LuckKidsException(ErrorCode.LUCKKIDS_CHARACTER_UNKNOWN));
	}

	public LuckkidsCharacter findRandomLuckkidsCharacterLevel1() {
		return luckkidsCharacterRepository.findRandomLevel1()
			.orElseThrow(() -> new LuckKidsException(ErrorCode.LUCKKIDS_CHARACTER_UNKNOWN));
	}

	public LuckCharacterRandResponse findRandomCharacterLevel1() {
		return LuckCharacterRandResponse.of(findRandomLuckkidsCharacterLevel1());
	}

}
