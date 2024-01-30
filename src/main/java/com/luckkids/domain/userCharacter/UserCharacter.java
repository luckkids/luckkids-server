package com.luckkids.domain.userCharacter;

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

    @Enumerated(EnumType.STRING)
    private CharacterProgressStatus characterProgressStatus;

    private String lottieFile;

    private String imageFile;

    @Builder
    public UserCharacter(User user, CharacterProgressStatus characterProgressStatus, String lottieFile, String imageFile) {
        this.user = user;
        this.characterProgressStatus = characterProgressStatus;
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }

    public void completeCharacter() {
        this.characterProgressStatus = CharacterProgressStatus.COMPLETED;
    }

    public void updateFiles(String lottieFile, String imageFile) {
        this.lottieFile = lottieFile;
        this.imageFile = imageFile;
    }
}