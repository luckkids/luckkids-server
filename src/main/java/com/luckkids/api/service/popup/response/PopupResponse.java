package com.luckkids.api.service.popup.response;

import com.luckkids.domain.popup.Popup;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PopupResponse {

    private final String label;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final List<PopupButtonResponse> buttons;

    @Builder
    private PopupResponse(String label, String title, String description, String imageUrl, List<PopupButtonResponse> buttons) {
        this.label = label;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.buttons = buttons;
    }

    public static PopupResponse createResponse(Popup popup) {
        return PopupResponse.builder()
                .label(popup.getLabel())
                .title(popup.getTitle())
                .description(convertNewLine(popup.getDescription()))
                .imageUrl(popup.getImageUrl())
                .buttons(popup.getButtons().stream()
                        .map(PopupButtonResponse::createResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private static String convertNewLine(String text) {
        if (text == null) {
            return null;
        }
        return text.replace("\\n", "\n");
    }
}
