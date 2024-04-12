package com.luckkids.api.service.notice;

import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.domain.notice.NoticeRepository;
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

    public List<NoticeResponse> getNoticeList(){
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"))
                .stream().map(NoticeResponse::of).collect(Collectors.toList());
    }
}
