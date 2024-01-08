package com.luckkids.domain.friend;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.friends.Friend;
import com.luckkids.domain.friends.FriendRepository;
import com.luckkids.domain.friends.FriendStatus;
import com.luckkids.domain.push.Push;
import com.luckkids.domain.push.PushRepository;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class FriendRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Test
    @DisplayName("사용자의 푸시목록을 삭제한다.")
    void deleteTest(){
        User user = User.builder()
            .email("test@email.com")
            .password("1234")
            .snsType(SnsType.NORMAL)
            .build();

        User user2 = User.builder()
            .email("test2@email.com")
            .password("12345")
            .snsType(SnsType.NORMAL)
            .build();

        userRepository.save(user);
        userRepository.save(user2);

        Friend friend = Friend.builder()
            .requester(user)
            .receiver(user2)
            .friendStatus(FriendStatus.ACCEPTED)
            .build();

        Friend friend2 = Friend.builder()
            .requester(user2)
            .receiver(user)
            .friendStatus(FriendStatus.REQUESTED)
            .build();

        friendRepository.save(friend);
        friendRepository.save(friend2);

        friendRepository.deleteAllByRequesterId(user.getId());
        friendRepository.deleteAllByReceiverId(user.getId());

        List<Friend> list = friendRepository.findAll();

        assertThat(list).hasSize(0);
    }
}
