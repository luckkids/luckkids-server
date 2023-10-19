package com.luckkids.domain.user;

import com.luckkids.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<Mission> missions = new ArrayList<>();
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<MissionComplete> missionCompletes = new ArrayList<>();
     * @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
     * private List<Friend> friends = new ArrayList<>();
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<AlertHistory> alertHistories = new ArrayList<>();
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     * private List<Push> pushes = new ArrayList<>();
     **/

    public void checkSnsType() {
        snsType.checkSnsType();
    }
    @Builder
    private User(String email, String password, SnsType snsType, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.snsType = snsType;
        this.phoneNumber = phoneNumber;
    }
}
