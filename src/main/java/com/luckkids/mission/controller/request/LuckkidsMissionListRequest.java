package com.luckkids.mission.controller.request;

import java.util.List;

import com.luckkids.mission.service.request.LuckkidsMissionListServiceRequest;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
