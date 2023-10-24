package com.luckkids.api.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    public int getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Optional.of(principal)
            .filter(p -> p instanceof UserDetails)
            .map(p -> Integer.parseInt(((UserDetails) p).getUsername()))
            .orElseGet(() -> {
                if (principal instanceof String) {
                    return Integer.parseInt((String) principal);
                } else {
                    throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
                }
            });
    }
}
