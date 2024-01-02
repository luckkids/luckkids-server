package com.luckkids.domain.push;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PushRepository extends JpaRepository<Push, Long> {
    void deleteAllByUserId(int userId);
}
