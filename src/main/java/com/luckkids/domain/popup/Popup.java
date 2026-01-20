package com.luckkids.domain.popup;

import com.luckkids.domain.BaseTimeEntity;
import com.luckkids.domain.popupButton.PopupButton;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Popup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String label;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PopupButton> buttons = new ArrayList<>();

    @Builder
    private Popup(String label, String title, String description, String imageUrl) {
        this.label = label;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
