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
    @Min(value = 1, message = "페이지는 1이상부터 입력가능합니다.")
    private int page;
    @Min(value = 1,message = "페이지사이즈는 1이상부터 입력가능합니다.")
    private int size;

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
