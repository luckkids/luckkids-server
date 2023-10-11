package com.luckkids.domain.user;

import com.luckkids.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String phoneNumber;

    /**
     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     private List<Mission> missions = new ArrayList<>();

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     private List<MissionComplete> missionCompletes = new ArrayList<>();

     @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
     private List<Friend> friends = new ArrayList<>();

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     private List<AlertHistory> alertHistories = new ArrayList<>();

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     private List<Push> pushes = new ArrayList<>();
     **/
}
