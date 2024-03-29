package com.luckkids.domain.misson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    List<Mission> findAllByUserIdAndDeletedDateIsNull(int userId);

    Optional<Mission> findByIdAndDeletedDateIsNull(int id);

    List<Mission> findAllByDeletedDateIsNull();

    void deleteAllByUserId(int userId);
}
