package com.luckkids.api.controller.friend;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.friend.request.FriendCreateRequest;
import com.luckkids.api.controller.request.PageInfoRequest;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friendCode.response.FriendCreateResponse;
import com.luckkids.api.service.friendCode.response.FriendInviteCodeResponse;
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

    @GetMapping("/list")
    public ApiResponse<PageCustom<FriendListReadResponse>> readListFriend(PageInfoRequest page) {
        return ApiResponse.ok(friendReadService.readListFriend(page.toServiceRequest()));
    }

    @GetMapping("/profile/{friendId}")
    public ApiResponse<FriendProfileReadResponse> profile(@PathVariable("friendId") int friendId) {
        return ApiResponse.ok(friendReadService.readProfile(friendId));
    }


}
