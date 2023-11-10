package com.luckkids.api.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PageableCustom {
    private int currentPage;
    private int totalPages;
    private long totalElements;

    @Builder
    private PageableCustom(int currentPage, int totalPages, long totalElements) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static <T> PageableCustom of(Page<T> page) {
        return PageableCustom.builder()
            .currentPage(page.getNumber() + 1)
            .totalPages(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .build();
    }
}
