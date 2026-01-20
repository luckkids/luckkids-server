package com.luckkids.api.controller.popup;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.service.popup.PopupReadService;
import com.luckkids.api.service.popup.response.PopupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PopupController {

    private final PopupReadService popupReadService;

    @GetMapping("/api/v1/popup")
    public ApiResponse<PopupResponse> getLatestPopup() {
        return ApiResponse.ok(popupReadService.getLatestPopup());
    }

}
