package com.luckkids.domain.userCharacter;

import com.luckkids.domain.luckkidsCharacter.LuckkidsCharacter;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private LuckkidsCharacter luckkidsCharacter;

    @Enumerated(EnumType.STRING)
    private CharacterProgressStatus characterProgressStatus;

    @Builder
    public UserCharacter(User user, CharacterProgressStatus characterProgressStatus, LuckkidsCharacter luckkidsCharacter) {
        this.user = user;
        this.characterProgressStatus = characterProgressStatus;
        this.luckkidsCharacter = luckkidsCharacter;
    }

    public void updateCompleteCharacter() {
        this.characterProgressStatus = CharacterProgressStatus.COMPLETED;
    }

    public void updateLuckkidsCharacter(LuckkidsCharacter luckkidsCharacter) {
        this.luckkidsCharacter = luckkidsCharacter;
    }
}