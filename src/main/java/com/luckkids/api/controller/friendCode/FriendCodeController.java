package com.luckkids.api.controller.friendCode;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.friendCode.request.FriendCodeNickNameRequest;
import com.luckkids.api.controller.friendCode.request.FriendCreateRequest;
import com.luckkids.api.service.friendCode.FriendCodeReadService;
import com.luckkids.api.service.friendCode.FriendCodeService;
import com.luckkids.api.service.friendCode.response.FriendCodeResponse;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friendcode")
public class FriendCodeController {

    private final FriendCodeService friendCodeService;
    private final FriendCodeReadService friendCodeReadService;

    @GetMapping
    public ApiResponse<FriendInviteCodeResponse> inviteCode() {
        return ApiResponse.ok(friendCodeService.inviteCode());
    }

    @GetMapping
    public ApiResponse<FriendCodeResponse> findByCode(@ModelAttribute @Valid FriendCodeNickNameRequest friendCodeNickNameRequest) {
        return ApiResponse.ok(friendCodeReadService.findNickNameByCode(friendCodeNickNameRequest.toServiceRequest()));
    }

    @PostMapping("/create")
    public ApiResponse<FriendCreateResponse> create(@RequestBody @Valid FriendCreateRequest friendCreateRequest) {
        return ApiResponse.ok(friendCodeService.create(friendCreateRequest.toServiceRequest()));
    }
}
