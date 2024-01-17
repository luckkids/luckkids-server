package com.luckkids.api.page.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PageableCustom {
    private int currentPage;
    private int totalPage;
    private long totalElement;

    @Builder
    private PageableCustom(int currentPage, int totalPage, long totalElement) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalElement = totalElement;
    }

    public static <T> PageableCustom of(Page<T> page) {
        return PageableCustom.builder()
            .currentPage(page.getNumber() + 1)
            .totalPage(page.getTotalPages())
            .totalElement(page.getTotalElements())
            .build();
    }
}
