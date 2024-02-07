package com.luckkids.domain.luckkidsMission;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LuckkidsMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    private LocalTime alertTime;

    @Builder
    private LuckkidsMission(int id, String description, LocalTime alertTime) {
        this.id = id;
        this.description = description;
        this.alertTime = alertTime;
    }
}
