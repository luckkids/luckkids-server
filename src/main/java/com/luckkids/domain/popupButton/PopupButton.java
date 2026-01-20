package com.luckkids.domain.popupButton;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.popup.Popup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupButton extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Popup popup;

    private String bgColor;

    private String link;

    private String text;

    private String textColor;

    @Builder
    private PopupButton(Popup popup, String bgColor, String link, String text, String textColor) {
        this.popup = popup;
        this.bgColor = bgColor;
        this.link = link;
        this.text = text;
        this.textColor = textColor;
    }

}
