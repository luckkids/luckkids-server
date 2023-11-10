package com.luckkids.api.service.notice;

import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeSaveResponse saveNotice(NoticeSaveServiceRequest noticeSaveRequest){
        Notice notice = noticeSaveRequest.toEntity();
        Notice savedNotice = noticeRepository.save(notice);
        return NoticeSaveResponse.of(savedNotice);
    }
}
