package com.luckkids.api.controller.version;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.version.request.VersionSaveRequest;
import com.luckkids.api.service.version.VersionReadService;
import com.luckkids.api.service.version.VersionService;
import com.luckkids.api.service.version.response.VersionResponse;
import com.luckkids.api.service.version.response.VersionSaveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/versions")
public class VersionController {

    private final VersionReadService versionReadService;
    private final VersionService versionService;

    @GetMapping
    public ApiResponse<VersionResponse> getVersion(){
        return ApiResponse.ok(versionReadService.getVersion());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<VersionSaveResponse> createVersion(@Valid @RequestBody VersionSaveRequest versionSaveRequest){
        return ApiResponse.created(versionService.save(versionSaveRequest.toServiceRequest()));
    }
}
