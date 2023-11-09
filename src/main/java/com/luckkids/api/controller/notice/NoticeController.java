package com.luckkids.api.controller.notice;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.notice.request.NoticeSaveRequest;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.NoticeService;
import com.luckkids.api.service.notice.response.NoticeDetailResponse;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeReadService noticeReadService;
    private final NoticeService noticeService;

    @GetMapping("/{id}")
    public ApiResponse<NoticeDetailResponse> getNotice(@PathVariable(value = "id") int id){
        return ApiResponse.ok(noticeReadService.getNotice(id));
    }

    @GetMapping("/list")
    public ApiResponse<List<NoticeResponse>> getNoticeList(){
        return ApiResponse.ok(noticeReadService.getNoticeList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public ApiResponse<NoticeSaveResponse> saveNotice(@Valid @RequestBody NoticeSaveRequest noticeSaveRequest){
        return ApiResponse.created(noticeService.saveNotice(noticeSaveRequest.toServiceRequest()));
    }
}
