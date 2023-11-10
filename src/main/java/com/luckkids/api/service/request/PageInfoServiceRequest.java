package com.luckkids.api.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
public class PageInfoServiceRequest {

    private int page;
    private int size;

    @Builder
    private PageInfoServiceRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Pageable toPageable(){
        return PageRequest.of(page - 1, size);
    }
}
