package com.luckkids.api.service.initialCharacter;

import com.luckkids.api.service.initialCharacter.response.InitialCharacterRandResponse;
import com.luckkids.domain.character.LuckkidsCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InitialCharacterService {

    private final LuckkidsCharacterRepository luckkidsCharacterRepository;
    @Value("${cloudfront.s3-url}")
    private String cloudFrontUrl;

    public List<InitialCharacterRandResponse> findAll(){
        return luckkidsCharacterRepository.findAll()
            .stream()
            .map(character -> InitialCharacterRandResponse.of(character, cloudFrontUrl))
            .toList();
    }

}
