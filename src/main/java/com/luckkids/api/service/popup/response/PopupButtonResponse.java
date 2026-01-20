package com.luckkids.api.service.popup.response;

import com.luckkids.domain.popupButton.PopupButton;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PopupButtonResponse {

    private final String bgColor;
    private final String link;
    private final String text;
    private final String textColor;

    @Builder
    private PopupButtonResponse(String bgColor, String link, String text, String textColor) {
        this.bgColor = bgColor;
        this.link = link;
        this.text = text;
        this.textColor = textColor;
    }

    public static PopupButtonResponse createResponse(PopupButton button) {
        return PopupButtonResponse.builder()
                .bgColor(button.getBgColor())
                .link(button.getLink())
                .text(button.getText())
                .textColor(button.getTextColor())
                .build();
    }
}
