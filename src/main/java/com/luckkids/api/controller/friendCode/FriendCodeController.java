package com.luckkids.api.controller.friendCode;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.friendCode.request.FriendCreateRequest;
import com.luckkids.api.service.friendCode.FriendCodeReadService;
import com.luckkids.api.service.friendCode.FriendCodeService;
import com.luckkids.api.service.friendCode.request.FriendCodeNickNameServiceRequest;
import com.luckkids.api.service.friendCode.response.FriendCodeNickNameResponse;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
import com.luckkids.api.service.friendCode.response.FriendRefuseResponse;
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

    @GetMapping("/{code}/nickname")
    public ApiResponse<FriendCodeNickNameResponse> findNickNameByCode(@PathVariable String code) {
        return ApiResponse.ok(friendCodeReadService.findNickNameByCode(
                FriendCodeNickNameServiceRequest.builder()
                        .code(code)
                        .build()));
    }

    @PostMapping("/create")
    public ApiResponse<FriendCreateResponse> create(@RequestBody @Valid FriendCreateRequest friendCreateRequest) {
        return ApiResponse.ok(friendCodeService.create(friendCreateRequest.toServiceRequest()));
    }

    @PostMapping("/{code}/refuse")
    public ApiResponse<FriendRefuseResponse> refuseFriend(@PathVariable String code) {
        return ApiResponse.ok(friendCodeService.refuseFriend(code));
    }
}
