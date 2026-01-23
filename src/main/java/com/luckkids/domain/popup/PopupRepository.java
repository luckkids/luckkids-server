package com.luckkids.domain.popup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopupRepository extends JpaRepository<Popup, Integer> {
    
    Optional<Popup> findFirstByOrderByCreatedDateDesc();
}
