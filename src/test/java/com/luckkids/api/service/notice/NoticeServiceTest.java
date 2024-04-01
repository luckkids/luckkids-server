package com.luckkids.api.service.notice;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAllInBatch();
    }

    @DisplayName(value = "공지사항을 저장한다.")
    @Test
    void saveNotice() {
        NoticeSaveServiceRequest noticeSaveServiceRequest = NoticeSaveServiceRequest.builder()
            .title("공지사항 타이틀")
            .url("www.naver.com")
            .build();

        NoticeSaveResponse savedNotice = noticeService.saveNotice(noticeSaveServiceRequest);

        Notice notice = noticeRepository.findById(savedNotice.getId()).get();
        assertThat(notice)
            .extracting("title", "url")
            .contains("공지사항 타이틀", "www.naver.com");
    }

}
