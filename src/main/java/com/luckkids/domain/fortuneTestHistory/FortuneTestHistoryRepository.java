package com.luckkids.domain.fortuneTestHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FortuneTestHistoryRepository extends JpaRepository<FortuneTestHistory, Integer> {

    Optional<FortuneTestHistory> findByUuid(String uuid);

}
