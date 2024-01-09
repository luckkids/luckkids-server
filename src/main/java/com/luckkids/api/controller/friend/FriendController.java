package com.luckkids.api.controller.friend;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.friend.request.FriendCreateRequest;
import com.luckkids.api.controller.request.PageInfoRequest;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.FriendService;
import com.luckkids.api.service.friend.response.FriendCreateResponse;
import com.luckkids.api.service.friend.response.FriendInviteCodeResponse;
import com.luckkids.api.service.friend.response.FriendListReadResponse;
import com.luckkids.api.service.friend.response.FriendProfileReadResponse;
import com.luckkids.api.service.response.PageCustom;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendReadService friendReadService;
    private final FriendService friendService;

    @GetMapping("/list")
    public ApiResponse<PageCustom<FriendListReadResponse>> readListFriend(PageInfoRequest page) {
        return ApiResponse.ok(friendReadService.readListFriend(page.toServiceRequest()));
    }

    @GetMapping("/profile/{friendId}")
    public ApiResponse<FriendProfileReadResponse> profile(@PathVariable("friendId") int friendId) {
        return ApiResponse.ok(friendReadService.readProfile(friendId));
    }

    @GetMapping("/inviteCode")
    public ApiResponse<FriendInviteCodeResponse> inviteCode() {
        return ApiResponse.ok(friendService.inviteCode());
    }

    @PostMapping("/create")
    public ApiResponse<FriendCreateResponse> create(@RequestBody @Valid FriendCreateRequest friendCreateRequest) {
        return ApiResponse.ok(friendService.create(friendCreateRequest.toServiceRequest()));
    }
}
