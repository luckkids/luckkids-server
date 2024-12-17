package com.luckkids.api.service.luckkidsMission.response;

import com.luckkids.domain.luckkidsMission.LuckkidsMission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LuckkidsMissionListSaveResponse {

    private List<LuckkidsMissionSaveResponse> missions;

    @Builder
    public LuckkidsMissionListSaveResponse(List<LuckkidsMissionSaveResponse> missions) {
        this.missions = missions;
    }

    public static LuckkidsMissionListSaveResponse of(List<LuckkidsMission> luckkidsMissions) {
        return LuckkidsMissionListSaveResponse.builder()
                .missions(luckkidsMissions.stream().map(LuckkidsMissionSaveResponse::of).toList())
                .build();
    }
}
