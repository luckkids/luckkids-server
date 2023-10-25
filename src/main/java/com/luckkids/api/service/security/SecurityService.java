package com.luckkids.api.service.security;

import com.luckkids.jwt.dto.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public UserInfo getCurrentUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        }

        throw new RuntimeException("Unknown principal type: " + principal.getClass().getName());
    }
}
