package com.luckkids.api.controller.request;

import com.luckkids.api.service.request.PageInfoServiceRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class PageInfoRequest {

    //기본값
    private int page = 1;
    private int size = 10;

    @Builder
    private PageInfoRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageInfoServiceRequest toServiceRequest(){
        return PageInfoServiceRequest.builder()
            .page(page)
            .size(size)
            .build();
    }
}
