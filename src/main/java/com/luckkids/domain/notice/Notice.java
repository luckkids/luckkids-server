package com.luckkids.domain.notice;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.luckkids.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String noticeDescription;

    @Builder
    private Notice(String noticeDescription) {
        this.noticeDescription = noticeDescription;
    }
}
