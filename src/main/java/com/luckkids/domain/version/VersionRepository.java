package com.luckkids.domain.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository extends JpaRepository<Version, Integer> {

    @Query("select v from Version v order by v.createdDate desc limit 1")
    public Version findVersionMax();
}
