package com.luckkids.domain.userAgreement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAgreementRepository extends JpaRepository<UserAgreement, Integer> {

    void deleteAllByUserId(int requestId);

}
