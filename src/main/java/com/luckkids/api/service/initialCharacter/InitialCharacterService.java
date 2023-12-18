package com.luckkids.api.service.initialCharacter;

import com.luckkids.api.service.initialCharacter.response.InitialCharacterRandResponse;
import com.luckkids.domain.initialCharacter.InitialCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InitialCharacterService {

    private final InitialCharacterRepository initialCharacterRepository;
    @Value("${cloudfront.s3-url}")
    private String cloudFrontUrl;

    public List<InitialCharacterRandResponse> findAll(){
        return initialCharacterRepository.findAll()
            .stream()
            .map(initialCharacter -> InitialCharacterRandResponse.of(initialCharacter, cloudFrontUrl))
            .toList();
    }

}
