package com.luckkids.api.service.notice;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.notice.response.NoticeDetailResponse;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName(value = "공지사항을 조회한다.")
    @Test
    void getNoticeTest() {
        Notice savedNotice = createNotice(1);

        NoticeDetailResponse noticeResponses = noticeReadService.getNotice(savedNotice.getId());

        assertThat(noticeResponses)
            .extracting("title", "noticeDescription")
            .contains("공지사항 타이틀1", "공지사항 테스트1");
    }

    @DisplayName(value = "공지사항을 조회시 해당 공지사항이 없으면 예외가 발생한다.")
    @Test
    void getNoticeWithoutNoticeTest() {
        assertThatThrownBy(() -> noticeReadService.getNotice(1))
            .isInstanceOf(LuckKidsException.class)
            .hasMessage("존재하지 않는 공지사항입니다.");
    }

    Notice createNotice(int i) {
        Notice notice = Notice.builder()
            .title("공지사항 타이틀" + i)
            .noticeDescription("공지사항 테스트" + i)
            .build();

        return noticeRepository.save(notice);
    }

}
