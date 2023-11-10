package com.luckkids.domain.notice;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    public List<Notice> findAll(Sort sort);
}
