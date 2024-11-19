package com.luckkids.api.service.user;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.delete.UserAgreementDeleteService;
import com.luckkids.api.service.user.delete.UserAlertDeleteService;
import com.luckkids.api.service.user.delete.UserCharacterDeleteService;
import com.luckkids.api.service.user.delete.UserFriendCodeDeleteService;
import com.luckkids.api.service.user.delete.UserFriendDeleteService;
import com.luckkids.api.service.user.delete.UserMissionDeleteService;
import com.luckkids.api.service.user.delete.UserTokenDeleteService;
import com.luckkids.api.service.user.request.UserUpdateLuckPhraseServiceRequest;
import com.luckkids.api.service.user.request.UserUpdateNicknameServiceRequest;
import com.luckkids.api.service.user.request.UserUpdatePasswordServiceRequest;
import com.luckkids.api.service.user.response.UserUpdateLuckPhraseResponse;
import com.luckkids.api.service.user.response.UserUpdateNicknameResponse;
import com.luckkids.api.service.user.response.UserUpdatePasswordResponse;
import com.luckkids.api.service.user.response.UserWithdrawResponse;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.user.User;
import com.luckkids.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final UserReadService userReadService;
	private final MissionOutcomeReadService missionOutcomeReadService;
	private final SecurityService securityService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserCharacterDeleteService userCharacterDeleteService;
	private final UserAlertDeleteService userAlertDeleteService;
	private final UserTokenDeleteService userTokenDeleteService;
	private final UserMissionDeleteService userMissionDeleteService;
	private final UserFriendDeleteService userFriendDeleteService;
	private final UserFriendCodeDeleteService userFriendCodeDeleteService;
	private final UserAgreementDeleteService userAgreementDeleteService;

	public UserUpdatePasswordResponse updatePassword(
		UserUpdatePasswordServiceRequest userUpdatePasswordServiceRequest) {
		User user = userReadService.findByEmail(userUpdatePasswordServiceRequest.getEmail());
		user.updatePassword(bCryptPasswordEncoder.encode(userUpdatePasswordServiceRequest.getPassword()));
		return UserUpdatePasswordResponse.of(user);
	}

	public UserUpdateLuckPhraseResponse updatePhrase(
		UserUpdateLuckPhraseServiceRequest userUpdateLuckPhraseServiceRequest) {
		User user = getCurrentUser();
		user.updateLuckPhrase(userUpdateLuckPhraseServiceRequest.getLuckPhrase());
		return UserUpdateLuckPhraseResponse.of(user);
	}

	public UserUpdateNicknameResponse updateNickname(
		UserUpdateNicknameServiceRequest userUpdateNicknameServiceRequest) {
		User user = getCurrentUser();
		String nickname = userUpdateNicknameServiceRequest.getNickname();
		user.updateNickName(nickname);
		return UserUpdateNicknameResponse.of(nickname);
	}

	public void minusMissionCount(int missionId, LocalDate missionDate) {
		MissionStatus missionOutcomeStatus = missionOutcomeReadService.getMissionOutcomeStatus(missionId, missionDate);
		if (missionOutcomeStatus.equals(MissionStatus.SUCCEED)) {
			User user = getCurrentUser();
			user.minusMissionCount();
		}
	}

	public UserWithdrawResponse withdraw() {
		User user = getCurrentUser();
		int userId = user.getId();

		userCharacterDeleteService.deleteAllByUserId(userId);
		userAlertDeleteService.deleteAllByUserId(userId);
		userTokenDeleteService.deleteAllByUserId(userId);
		userMissionDeleteService.deleteAllByUserId(userId);
		userFriendDeleteService.deleteAllByUserId(userId);
		userFriendCodeDeleteService.deleteAllByUserId(userId);
		userAgreementDeleteService.deleteAllByUserId(userId);
		userRepository.deleteById(userId);

		return UserWithdrawResponse.of(userId);
	}

	private User getCurrentUser() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return userReadService.findByOne(userId);
	}

}
