package com.luckkids.api.controller.friend;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.page.request.PageInfoRequest;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.response.FriendListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendReadService friendReadService;

    @GetMapping("/api/v1/friends")
    public ApiResponse<FriendListResponse> getFriendList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(friendReadService.getFriendList(request.toServiceRequest()));
    }


}
