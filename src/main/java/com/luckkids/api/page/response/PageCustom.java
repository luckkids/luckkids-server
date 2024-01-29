package com.luckkids.api.page.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class PageCustom<T> implements Serializable {
    private List<T> content;
    private PageableCustom pageInfo;

    @Builder
    private PageCustom(List<T> content, PageableCustom pageInfo) {
        this.content = content;
        this.pageInfo = pageInfo;
    }

    public static <T> PageCustom<T> of(Page<T> page) {
        return PageCustom.<T>builder()
            .content(page.getContent())
            .pageInfo(PageableCustom.of(page))
            .build();
    }
}
