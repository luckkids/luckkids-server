package com.luckkids.domain.clover;

import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Clover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    private int count;

    @Builder
    private Clover(User user, int count) {
        this.user = user;
        this.count = count;
    }

    public void changeUser(User user){
        this.user = user;
    }
}
