package com.luckkids.api.page.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class PageInfoServiceRequest {

	private int page;
	private int size;

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size);
	}
}
