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

    private String characterNickname;

    private String file;

    @Builder
    private UserCharacter(User user, int level, String characterNickname, String file) {
        this.user = user;
        this.level = level;
        this.characterNickname = characterNickname;
        this.file = file;
    }

    public void changeUser(User user) {
        this.user = user;
    }
}
