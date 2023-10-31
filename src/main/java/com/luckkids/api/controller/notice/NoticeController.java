package com.luckkids.api.controller.notice;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.response.NoticeResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {

    private final NoticeReadService noticeReadService;

    @GetMapping("/{id}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable(value = "id") int id){
        return ApiResponse.ok(noticeReadService.getNotice(id));
    }

    @GetMapping("/list")
    public ApiResponse<List<NoticeResponse>> getNoticeList(){
        return ApiResponse.ok(noticeReadService.getNoticeList());
    }
}
