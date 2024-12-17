package com.luckkids.api.controller.luckkidsMission;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.luckkidsMission.request.LuckkidsMissionListRequest;
import com.luckkids.api.service.luckkidsMission.LuckkidsMissionService;
import com.luckkids.api.service.luckkidsMission.response.LuckkidsMissionListSaveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/luckkidsMission")
public class LuckkidsMissionController {

    private final LuckkidsMissionService luckkidsMissionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<LuckkidsMissionListSaveResponse> createLuckkidsMission(@Valid @RequestBody LuckkidsMissionListRequest luckkidsMissionListRequest) {
        return ApiResponse.created(luckkidsMissionService.createLuckkidsMission(luckkidsMissionListRequest.toServiceRequest()));
    }

}
