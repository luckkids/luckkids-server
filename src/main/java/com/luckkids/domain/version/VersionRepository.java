package com.luckkids.domain.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository extends JpaRepository<Version, Integer> {

    @Query("select v from Version v where v.createdDate = (select max(v2.createdDate) from Version v2)")
    public Version findVersionMax();
}
