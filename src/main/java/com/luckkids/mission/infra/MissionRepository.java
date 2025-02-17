package com.luckkids.mission.infra;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luckkids.mission.domain.misson.Mission;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

	List<Mission> findAllByUserIdAndDeletedDateIsNullOrderByMissionActiveDescAlertTimeAsc(int userId);

	Optional<Mission> findByIdAndDeletedDateIsNull(int id);

	List<Mission> findAllByDeletedDateIsNull();

	void deleteAllByUserId(int userId);
}
