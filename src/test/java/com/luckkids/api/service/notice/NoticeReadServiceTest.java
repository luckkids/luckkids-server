package com.luckkids.api.service.notice;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private NoticeReadService noticeReadService;

    @Autowired
    private NoticeRepository noticeRepository;

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAllInBatch();
    }

    @DisplayName(value = "공지사항 목록을 조회한다.")
    @Test
    void getNoticeListTest() {
        for (int i = 1; i <= 3; i++) {
            createNotice(i);
        }

        List<NoticeResponse> noticeResponses = noticeReadService.getNoticeList();

        assertThat(noticeResponses).hasSize(3)
            .extracting("title")
            .contains(
                "공지사항 타이틀1",
                "공지사항 타이틀2",
                "공지사항 타이틀3"
            );
    }


    Notice createNotice(int i) {
        Notice notice = Notice.builder()
            .title("공지사항 타이틀" + i)
            .url("www.naver" + i + ".com")
            .build();

        return noticeRepository.save(notice);
    }

}
