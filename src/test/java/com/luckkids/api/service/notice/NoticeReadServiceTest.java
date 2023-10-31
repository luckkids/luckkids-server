package com.luckkids.api.service.notice;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.notice.response.NoticeResponse;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import com.mysema.commons.lang.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

import static com.luckkids.domain.misson.AlertStatus.UNCHECKED;
import static com.mysema.commons.lang.Assert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

public class NoticeReadServiceTest extends IntegrationTestSupport {

    @Autowired
    NoticeReadService noticeReadService;
    @Autowired
    NoticeRepository noticeRepository;

    @DisplayName(value = "공지사항 목록을 조회한다.")
    @Test
    void getNoticeListTest(){
        for(int i=1; i<=3; i++){
            createNotice(i);
        }

        List<NoticeResponse> noticeResponses = noticeReadService.getNoticeList();

        assertThat(noticeResponses).hasSize(3)
                .extracting("noticeDescription")
                .containsExactlyInAnyOrder(
                        tuple("공지사항테스트1"),
                        tuple("공지사항테스트2"),
                        tuple("공지사항테스트3")
                );
    }

    @DisplayName(value = "공지사항을 조회한다.")
    @Test
    void getNoticeTest(){
        Notice savedNotice = createNotice(1);

        NoticeResponse noticeResponses = noticeReadService.getNotice(savedNotice.getId());

        assertThat(noticeResponses.getNoticeDescription()).isEqualTo("공지사항테스트1");
    }

    @DisplayName(value = "공지사항을 조회시 해당 공지사항이 없으면 예외가 발생한다.")
    @Test
    void getNoticeWithoutNoticeTest(){
        assertThatThrownBy(() -> noticeReadService.getNotice(1))
                .isInstanceOf(LuckKidsException.class)
                .hasMessage("존재하지 않는 공지사항입니다.");
    }

    Notice createNotice(int i){
        Notice notice = Notice.builder()
                .noticeDescription("공지사항 테스트" + i)
                .build();

        return noticeRepository.save(notice);
    }

}
