package com.luckkids.domain.misson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT m FROM Mission m WHERE m.user.id = :userId")
    List<Mission> findAllByUserId(int userId);

}
