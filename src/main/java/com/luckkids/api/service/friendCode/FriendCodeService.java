package com.luckkids.api.service.friendCode;

import com.luckkids.notification.service.AlertHistoryReadService;
import com.luckkids.notification.service.AlertHistoryService;
import com.luckkids.notification.service.AlertSettingReadService;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.request.FriendStatusRequest;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.request.FriendCreateServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.friendCode.response.FriendRefuseResponse;
import com.luckkids.notification.service.PushService;
import com.luckkids.notification.service.request.SendPushDataDto;
import com.luckkids.notification.service.request.SendPushServiceRequest;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.notification.domain.alertHistory.AlertDestinationType;
import com.luckkids.notification.domain.alertSetting.AlertSetting;
import com.luckkids.notification.domain.alertSetting.AlertType;
import com.luckkids.domain.friend.Friend;
import com.luckkids.domain.friend.FriendRepository;
import com.luckkids.domain.friendCode.FriendCode;
import com.luckkids.domain.friendCode.FriendCodeRepository;
import com.luckkids.domain.friendCode.FriendStatus;
import com.luckkids.domain.friendCode.UseStatus;
import com.luckkids.notification.domain.push.PushMessage;
import com.luckkids.domain.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendCodeService {

	private final SecurityService securityService;
	private final FriendCodeRepository friendCodeRepository;
	private final FriendRepository friendRepository;
	private final UserReadService userReadService;
	private final FriendCodeReadService friendCodeReadService;
	private final PushService pushService;
	private final AlertSettingReadService alertSettingReadService;
	private final FriendReadService friendReadService;
	private final AlertHistoryReadService alertHistoryReadService;
	private final AlertHistoryService alertHistoryService;

	public FriendInviteCodeResponse inviteCode() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		String code = "";
		do {
			code = generateCode();
		} while (friendCodeRepository.existsByCode(code));

		User user = userReadService.findByOne(userId);

		friendCodeRepository.save(
			FriendCode.builder()
				.user(user)
				.code(code)
				.useStatus(UseStatus.UNUSED)
				.build()
		);

		return FriendInviteCodeResponse.of(code);
	}

	public FriendCodeNickNameResponse findNickNameByCode(FriendCodeNickNameServiceRequest friendCreateServiceRequest) {
		int receiverId = securityService.getCurrentLoginUserInfo().getUserId(); // 수신자 ID
		String code = friendCreateServiceRequest.getCode();
		FriendCode friendCode = friendCodeReadService.findByCode(code);
		User friend = friendCode.getUser(); // 요청자 ID

		//처음으로 받은 요청이면 AlertHistory에 적재
		if (!alertHistoryReadService.hasFriendCode(receiverId, code)) {
			User user = userReadService.findByOne(receiverId);
			alertHistoryService.createAlertHistory(
				friendCreateServiceRequest.toAlertHistoryServiceRequest(user, friend.getNickname(), code));
		}

		// 내가 보낸 초대인지 체크
		if (friend.getId() == receiverId) {
			return FriendCodeNickNameResponse.of(friendCode.getUser().getNickname(), FriendStatus.ME);
		}

		// 이미 친구인지 체크
		boolean isAlreadyFriend = friendReadService.checkFriendStatus(
			FriendStatusRequest.of(friend.getId(), receiverId));
		FriendStatus friendStatus = isAlreadyFriend ? FriendStatus.ALREADY : FriendStatus.FRIEND;

		return FriendCodeNickNameResponse.of(friend.getNickname(), friendStatus);
	}

	public FriendCreateResponse create(FriendCreateServiceRequest friendCreateServiceRequest) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		FriendCode friendCode = friendCodeReadService.findByCode(friendCreateServiceRequest.getCode());

		friendCode.checkUsed();
		friendCode.updateUseStatus();

		User requestUser = friendCode.getUser();
		User receiveUser = userReadService.findByOne(userId);

		friendRepository.save(createFriend(requestUser, receiveUser));
		friendRepository.save(createFriend(receiveUser, requestUser));

		List<AlertSetting> alertSettingList = alertSettingReadService.findAllByUserId(requestUser.getId());

		alertSettingList.stream()
			.filter(alertSetting -> alertSetting.alertTypeChecked(AlertType.FRIEND))
			.forEach(alertSetting -> pushService.sendPushToUser(
				SendPushServiceRequest.builder()
					.push(alertSetting.getPush())
					.body(PushMessage.GARDEN.getText().replace("{nickName}", receiveUser.getNickname()))
					.sendPushDataDto(SendPushDataDto.builder()
						.alert_destination_type(AlertDestinationType.FRIEND)
						.alert_destination_info(String.valueOf(receiveUser.getId()))
						.build())
					.build()
			));

		return FriendCreateResponse.of(requestUser, receiveUser);
	}

	public FriendRefuseResponse refuseFriend(String code) {
		FriendCode friendCode = friendCodeReadService.findByCode(code);
		friendCode.updateUseStatus();
		return FriendRefuseResponse.of(code);
	}

	private String generateCode() {
		SecureRandom random = new SecureRandom();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return random.ints(8, 0, chars.length())
			.mapToObj(chars::charAt)
			.map(Object::toString)
			.collect(Collectors.joining());
	}

	private Friend createFriend(User requestUser, User receiveUser) {
		return Friend.builder()
			.requester(requestUser)
			.receiver(receiveUser)
			.build();
	}
}
