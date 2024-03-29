package com.luckkids.api.service.notice;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.mission.response.MissionResponse;
import com.luckkids.api.service.notice.response.NoticeDetailResponse;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeReadService {

    private final NoticeRepository noticeRepository;

    public NoticeDetailResponse getNotice(int id){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(()-> new LuckKidsException(ErrorCode.ALERT_UNKNOWN));

        return NoticeDetailResponse.of(notice);
    }

    public List<NoticeResponse> getNoticeList(){
        List<Notice> noticeList = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return noticeList.stream().map(NoticeResponse::of).collect(Collectors.toList());
    }
}
