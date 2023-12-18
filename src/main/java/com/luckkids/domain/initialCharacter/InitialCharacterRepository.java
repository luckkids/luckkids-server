package com.luckkids.domain.initialCharacter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InitialCharacterRepository extends JpaRepository<InitialCharacter, Integer> {
}
