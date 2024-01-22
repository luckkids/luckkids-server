package com.luckkids.api.service.luckkidsCharacter;

import com.luckkids.api.service.luckkidsCharacter.response.InitialCharacterRandResponse;
import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacterRepository;
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

    public List<InitialCharacterRandResponse> findAllByCharacterIdLevel1() {
        return luckkidsCharacterRepository.findAllByCharacterIdLevel(1)
            .stream()
            .map(character -> InitialCharacterRandResponse.of(character, cloudFrontUrl))
            .toList();
    }

}
