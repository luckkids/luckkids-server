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
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private int level;
    private String characterName;
    private String fileName;

    @Builder
    private UserCharacter(User user, int level, String characterName, String fileName) {
        this.user = user;
        this.level = level;
        this.characterName = characterName;
        this.fileName = fileName;
    }

    public void changeUser(User user){
        this.user = user;
    }
}
