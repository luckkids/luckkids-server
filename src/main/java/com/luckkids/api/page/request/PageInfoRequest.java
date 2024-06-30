package com.luckkids.api.page.request;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PageInfoRequest {

	@Default
	private int page = 1;

	@Default
	private int size = 12;

	public PageInfoServiceRequest toServiceRequest() {
		return PageInfoServiceRequest.builder()
			.page(page)
			.size(size)
			.build();
	}
}
