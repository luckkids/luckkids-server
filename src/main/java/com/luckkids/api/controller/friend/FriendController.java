package com.luckkids.api.controller.friend;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.request.PageInfoRequest;
import com.luckkids.api.service.friend.response.FriendProfileReadServiceResponse;
import com.luckkids.api.service.response.PageCustom;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.response.FriendListReadServiceResponse;
import com.luckkids.api.service.security.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendReadService friendReadService;
    private final SecurityService securityService;

    @GetMapping("/readlist")
    public ApiResponse<PageCustom<FriendListReadServiceResponse>> readListFriend(@Valid @RequestBody PageInfoRequest page){
        int userId = securityService.getCurrentUserInfo().getUserId();
        return ApiResponse.ok(friendReadService.readListFriend(userId, page.toServiceRequest()));
    }

    @GetMapping("/profile/{friendId}")
    public ApiResponse<FriendProfileReadServiceResponse> profile(@PathVariable("friendId") int friendId){
        return ApiResponse.ok(friendReadService.readProfile(friendId));
    }
}
