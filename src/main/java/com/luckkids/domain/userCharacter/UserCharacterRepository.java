package com.luckkids.domain.userCharacter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Integer> {

    void deleteAllByUserId(int userId);
}
