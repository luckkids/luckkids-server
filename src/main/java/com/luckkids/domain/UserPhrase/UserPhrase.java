package com.luckkids.domain.UserPhrase;

import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPhrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    private String phraseDescription;

    @Builder
    private UserPhrase(User user, String phraseDescription) {
        this.user = user;
        this.phraseDescription = phraseDescription;
    }

    public void changeUser(User user){
        this.user = user;
    }
}
