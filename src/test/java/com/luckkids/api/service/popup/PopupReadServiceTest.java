package com.luckkids.api.service.popup;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.luckkids.domain.popupButton.PopupButtonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.luckkids.IntegrationTestSupport;
import com.luckkids.api.service.popup.response.PopupResponse;
import com.luckkids.domain.popup.Popup;
import com.luckkids.domain.popup.PopupRepository;
import com.luckkids.domain.popupButton.PopupButton;

class PopupReadServiceTest extends IntegrationTestSupport {

    @Autowired
    private PopupReadService popupReadService;

    @Autowired
    private PopupRepository popupRepository;

    @Autowired
    private PopupButtonRepository popupButtonRepository;

    @AfterEach
    void tearDown() {
        popupButtonRepository.deleteAllInBatch();
        popupRepository.deleteAllInBatch();
    }

    @DisplayName("최신 팝업을 조회한다.")
    @Test
    void getLatestPopup() {
        // given
        Popup popup1 = createPopup("이벤트1", "첫번째 이벤트", "첫번째 이벤트 설명", "https://example.com/image1.png");

        Popup savedPopups = popupRepository.save(popup1);

        // 버튼 추가
        createPopupButton(savedPopups, "#FF5733", "https://example.com/action1", "확인", "#FFFFFF");
        createPopupButton(savedPopups, "#33FF57", "https://example.com/action2", "취소", "#000000");
        popupRepository.save(savedPopups);  // 버튼 추가 후 다시 저장

        // when
        PopupResponse result = popupReadService.getLatestPopup();

        // then
        assertThat(result).isNotNull()
                .extracting("label", "title", "description", "imageUrl")
                .containsExactly("이벤트1", "첫번째 이벤트", "첫번째 이벤트 설명", "https://example.com/image1.png");

        assertThat(result.getButtons())
                .hasSize(2)
                .extracting("bgColor", "link", "text", "textColor")
                .containsExactly(
                        tuple("#FF5733", "https://example.com/action1", "확인", "#FFFFFF"),
                        tuple("#33FF57", "https://example.com/action2", "취소", "#000000")
                );
    }

    @DisplayName("버튼이 없는 팝업을 조회한다.")
    @Test
    void getLatestPopupWithoutButtons() {
        // given
        Popup popup = createPopup("공지사항", "중요 공지", "공지사항 내용입니다.", "https://example.com/notice.png");
        popupRepository.save(popup);

        // when
        PopupResponse result = popupReadService.getLatestPopup();

        // then
        assertThat(result).isNotNull()
                .extracting("label", "title", "description", "imageUrl")
                .containsExactly("공지사항", "중요 공지", "공지사항 내용입니다.", "https://example.com/notice.png");

        assertThat(result.getButtons()).isEmpty();
    }

    private Popup createPopup(String label, String title, String description, String imageUrl) {
        return Popup.builder()
                .label(label)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }

    private PopupButton createPopupButton(Popup popup, String bgColor, String link, String text, String textColor) {
        PopupButton button = PopupButton.builder()
                .popup(popup)
                .bgColor(bgColor)
                .link(link)
                .text(text)
                .textColor(textColor)
                .build();
        popup.getButtons().add(button);
        return button;
    }
}
