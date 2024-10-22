package com.luckkids.domain.misson;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

	List<Mission> findAllByUserIdAndDeletedDateIsNullOrderByCreatedDateAsc(int userId);

	Optional<Mission> findByIdAndDeletedDateIsNull(int id);

	List<Mission> findAllByDeletedDateIsNull();

	void deleteAllByUserId(int userId);
}
