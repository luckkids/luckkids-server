package com.luckkids.api.page.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PageInfoRequest {

	private int page = 1;
	private int size = 12;

	public PageInfoServiceRequest toServiceRequest() {
		return PageInfoServiceRequest.builder()
			.page(page)
			.size(size)
			.build();
	}
}
