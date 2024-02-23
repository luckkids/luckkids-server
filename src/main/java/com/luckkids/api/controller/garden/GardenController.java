package com.luckkids.api.controller.garden;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.page.request.PageInfoRequest;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.friend.response.FriendListResponse;
import com.luckkids.api.service.user.UserReadService;
import com.luckkids.api.service.user.response.UserLeagueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GardenController {

    private final FriendReadService friendReadService;
    private final UserReadService userReadService;

    @GetMapping("/api/v1/garden/list")
    public ApiResponse<FriendListResponse> getFriendList(@ModelAttribute PageInfoRequest request) {
        return ApiResponse.ok(friendReadService.getFriendList(request.toServiceRequest()));
    }

    @GetMapping("/api/v1/garden/league")
    public ApiResponse<List<UserLeagueResponse>> getUserLeagueTop3() {
        return ApiResponse.ok(userReadService.getUserLeagueTop3());
    }
}
