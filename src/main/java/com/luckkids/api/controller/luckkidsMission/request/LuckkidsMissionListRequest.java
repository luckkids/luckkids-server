package com.luckkids.api.controller.luckkidsMission.request;

import com.luckkids.api.service.luckkidsMission.request.LuckkidsMissionListServiceRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LuckkidsMissionListRequest {

    @Valid
    private List<LuckkidsMissionRequest> missions;

    @Builder
    public LuckkidsMissionListRequest(List<LuckkidsMissionRequest> missions) {
        this.missions = missions;
    }

    public LuckkidsMissionListServiceRequest toServiceRequest() {
        return LuckkidsMissionListServiceRequest.builder()
                .missions(missions.stream().map(LuckkidsMissionRequest::toServiceRequest).toList())
                .build();
    }
}
