package com.luckkids.api.controller.notice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.notice.request.NoticeSaveRequest;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.NoticeService;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeReadService noticeReadService;
	private final NoticeService noticeService;

	@GetMapping("/api/v1/notices")
	public ApiResponse<List<NoticeResponse>> getNoticeList() {
		return ApiResponse.ok(noticeReadService.getNoticeList());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/notices/new")
	public ApiResponse<NoticeSaveResponse> createNotice(@Valid @RequestBody NoticeSaveRequest noticeSaveRequest) {
		return ApiResponse.created(noticeService.saveNotice(noticeSaveRequest.toServiceRequest()));
	}
}
