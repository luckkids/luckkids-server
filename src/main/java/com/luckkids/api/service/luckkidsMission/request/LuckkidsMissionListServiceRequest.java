package com.luckkids.api.service.luckkidsMission.request;

import com.luckkids.domain.luckkidsMission.LuckkidsMission;
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
