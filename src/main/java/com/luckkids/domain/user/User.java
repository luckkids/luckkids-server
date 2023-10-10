package com.luckkids.domain.user;

import com.luckkids.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String phoneNumber;

    /** 이거는 사실 response에서 사용될 것 같을 때 사용하는거라 사용 안된다면 안 써두 돼요 ! **/
//     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//     private List<Mission> missions = new ArrayList<>();
//
//     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//     private List<MissionComplete> missionCompletes = new ArrayList<>();
//
//     @OneToMany(mappedBy = "requesterId", cascade = CascadeType.ALL)
//     private List<Friend> friends = new ArrayList<>();
//
//     @OneToMany(mappedBy = "user")
//     private List<AlertHistory> alertHistories = new ArrayList<>();
//
//     @OneToMany(mappedBy = "user")
//     private List<Push> pushes = new ArrayList<>();
}
