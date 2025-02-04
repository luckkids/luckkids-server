package com.luckkids.notification.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.domain.user.SnsType;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;
import com.luckkids.notification.service.request.SendAuthCodeServiceRequest;
import com.luckkids.notification.service.request.SendPasswordServiceRequest;
import com.luckkids.notification.service.response.SendAuthUrlResponse;
import com.luckkids.notification.service.response.SendPasswordResponse;

public class MailServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@Test
	void sendAuthUrlTest() {
		SendAuthCodeServiceRequest sendAuthCodeServiceRequest = SendAuthCodeServiceRequest.builder()
			.email("tkdrl8908@naver.com")
			.build();

		given(mailService.sendAuthUrl(any(SendAuthCodeServiceRequest.class)))
			.willReturn(SendAuthUrlResponse.builder()
				.authKey("7MMfhzwplTsqvw")
				.build()
			);

		SendAuthUrlResponse sendAuthCodeResponse = mailService.sendAuthUrl(sendAuthCodeServiceRequest);

		assertThat(sendAuthCodeResponse.getAuthKey()).isEqualTo("7MMfhzwplTsqvw");
	}

	@Test
	void SendPasswordTest() {
		User user = createUser("tkdrl8908@test.com", "2134", SnsType.NORMAL);
		userRepository.save(user);
		SendPasswordServiceRequest sendPasswordServiceRequest = SendPasswordServiceRequest.builder()
			.email("tkdrl8908@test.com")
			.build();

		given(mailService.sendPassword(any(SendPasswordServiceRequest.class)))
			.willReturn(SendPasswordResponse.builder()
				.email("tkdrl8908@test.com")
				.build()
			);

		SendPasswordResponse sendPasswordResponse = mailService.sendPassword(sendPasswordServiceRequest);

		assertThat(sendPasswordResponse.getEmail()).isEqualTo("tkdrl8908@test.com");
	}

	//    @Test
	//    void generateCode() {
	//        given(mailService.generateCode())
	//            .willReturn("123456"
	//            );
	//
	//        String authNum = mailService.generateCode();
	//
	//        assertThat(authNum.length()).isEqualTo(6);
	//    }
	//
	//    @Test
	//    void generateTempPassword() {
	//        given(mailService.generateTempPassword())
	//            .willReturn("AsDWET2s24asASd"
	//            );
	//
	//        String tempPassword = mailService.generateTempPassword();
	//
	//        assertThat(tempPassword.length()).isEqualTo(15);
	//    }

	private User createUser(String email, String password, SnsType snsType) {
		return User.builder()
			.email(email)
			.password(password)
			.snsType(snsType)
			.build();
	}
}
