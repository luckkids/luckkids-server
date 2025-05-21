package com.luckkids.domain.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.IntegrationTestSupport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
class UserRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	@DisplayName("유저의 이메일로 유저 정보를 조회한다.")
	@Test
	void findByEmail() {
		// given
		String email = "user@daum.net";
		User user = createUser(email, "user1234!", "테스트1", "테스트1의 행운문구", 0);
		userRepository.save(user);

		// when
		User findUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));

		// then
		assertThat(findUser.getEmail()).isEqualTo(email);
	}

	@DisplayName("missionCount를 1씩 3번 증가시키고, 매번 1행 업데이트를 반환하며 최종적으로 +3이 반영된다")
	@Test
	void incrementMissionCount() {
		// given
		int initialCount = 5;
		User user = createUser("user@daum.net", "user1234!", "테스트1", "테스트1의 행운문구", initialCount);
		userRepository.save(user);

		int[] deltas = {+1, -1, +1, +1, -1};
		int expected = initialCount;

		// when
		for (int delta : deltas) {
			long updatedRows = userRepository.updateMissionCount(user.getId(), delta);
			assertThat(updatedRows)
				.isOne();

			expected += delta;
		}

		em.clear();

		// then: 최종 missionCount 가 initial + sum(deltas) 가 되어야 한다
		User refreshed = userRepository.findById(user.getId())
			.orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));
		assertThat(refreshed.getMissionCount())
			.isEqualTo(expected);
	}

	private User createUser(String email, String password, String nickname, String luckPhrase, int missionCount) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(SnsType.NORMAL)
			.nickname(nickname)
			.luckPhrase(luckPhrase)
			.role(Role.USER)
			.settingStatus(SettingStatus.COMPLETE)
			.missionCount(missionCount)
			.build();
	}
}