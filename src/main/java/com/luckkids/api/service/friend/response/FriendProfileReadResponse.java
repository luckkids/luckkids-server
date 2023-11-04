package com.luckkids.api.service.friend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendProfileReadResponse {

    private String phraseDescription;
    private String fileUrl;
    private String characterName;
    private int level;

    @Builder
    private FriendProfileReadResponse(String phraseDescription, String fileUrl, String characterName, int level) {
        this.phraseDescription = phraseDescription;
        this.fileUrl = fileUrl;
        this.characterName = characterName;
        this.level = level;
    }
}
