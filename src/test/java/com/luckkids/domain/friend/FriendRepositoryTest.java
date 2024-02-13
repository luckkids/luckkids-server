package com.luckkids.domain.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class FriendRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Test
    @DisplayName("사용자의 푸시목록을 삭제한다.")
    void deleteAllById() {
        User user = createUser("test@email.com", "1234", SnsType.NORMAL);
        User user2 = createUser("test2@email.com", "12345", SnsType.NORMAL);
        userRepository.saveAll(List.of(user, user2));

        userRepository.save(user);
        userRepository.save(user2);

        Friend friend = createFriend(user, user2);
        Friend friend2 = createFriend(user2, user);
        friendRepository.saveAll(List.of(friend, friend2));

        friendRepository.deleteAllByRequesterId(user.getId());
        friendRepository.deleteAllByReceiverId(user.getId());

        List<Friend> list = friendRepository.findAll();

        assertThat(list).hasSize(0);
    }

    private User createUser(String email, String password, SnsType snsType) {
        return User.builder()
            .email(email)
            .password(password)
            .snsType(snsType)
            .build();
    }

    private Friend createFriend(User requester, User receiver) {
        return Friend.builder()
            .requester(requester)
            .receiver(receiver)
            .build();
    }
}
