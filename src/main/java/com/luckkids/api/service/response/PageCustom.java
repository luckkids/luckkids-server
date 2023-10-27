package com.luckkids.api.service.response;

import com.luckkids.api.service.request.PageInfoServiceRequest;
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
    private  PageCustom (List<T> content, PageableCustom pageInfo) {
        this.content = content;
        this.pageInfo = pageInfo;
    }

    @Builder
    private PageCustom(List<T> content, Page<T> pageable) {
        this.content = content;
        this.pageInfo = PageableCustom.of(pageable);
    }

    public static <T> PageCustom<T> of(Page<T> page) {
        return new PageCustom<>(page.getContent(), page);
    }
}
