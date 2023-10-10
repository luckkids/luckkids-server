package com.luckkids.domain.misson;

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
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String missionDescription;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    private LocalDateTime alertDate;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

}
