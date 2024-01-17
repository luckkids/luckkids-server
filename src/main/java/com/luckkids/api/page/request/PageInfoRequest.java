package com.luckkids.api.page.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageInfoRequest {

    private int page = 1;
    private int size = 12;

    @Builder
    private PageInfoRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageInfoServiceRequest toServiceRequest() {
        return PageInfoServiceRequest.builder()
            .page(page)
            .size(size)
            .build();
    }
}
