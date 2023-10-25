package com.luckkids.domain.misson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT m FROM Mission m JOIN FETCH m.user u WHERE u.id = :userId")
    List<Mission> findAllJoinUser(int userId);

}
