package com.luckkids.domain.missonComplete;

import com.luckkids.domain.BaseEntity;
import com.luckkids.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MissionComplete extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private LocalDateTime missionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String missionDescription;

    //Persist시 당일날짜로 세팅
    @PrePersist
    public void prePersist() {
        missionDate = LocalDateTime.now();
    }
}
