package com.luckkids.mission.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LuckkidsMissionListServiceRequest {

    private List<LuckkidsMissionServiceRequest> missions;

    @Builder
    public LuckkidsMissionListServiceRequest(List<LuckkidsMissionServiceRequest> missions) {
        this.missions = missions;
    }
}
