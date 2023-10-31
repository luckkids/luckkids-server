package com.luckkids.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Query("select n from Notice n order by n.createdDate desc")
    public List<Notice> findAllOrderbyDesc();
}
