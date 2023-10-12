package com.luckkids.api.repository.login;

import com.luckkids.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<User, Long> {
    public Optional<User>  findByEmail(String email);
}
