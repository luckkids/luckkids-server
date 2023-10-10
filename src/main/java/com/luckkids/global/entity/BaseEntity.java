package com.luckkids.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * @description 테이블 데이터생성 혹은 수정시에 필요한 컬럼 모든 Entity에 상속하여 사용한다.
 * @author skhan
 */
@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity {
	/**
	 * 생성날짜
	 */
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
	/**
	 * 수정날짜
	 */
	@LastModifiedDate
	private LocalDateTime updatedDate;
	/**
	 * 생성자
	 */
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;
	/**
	 * 수정자
	 */
	@LastModifiedBy
	private String updatedBy;

}
