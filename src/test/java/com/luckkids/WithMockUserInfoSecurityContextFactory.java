package com.luckkids;

import com.luckkids.domain.user.WithMockUserInfo;
import com.luckkids.jwt.dto.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockUserInfoSecurityContextFactory implements WithSecurityContextFactory<WithMockUserInfo> {
    @Override
    public SecurityContext createSecurityContext(WithMockUserInfo annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        //UserInfo 생성 ID는 1로 그냥 넣었는데 혹시 필요하면 추가하시면됩니다..!!
        UserInfo userInfo = UserInfo.of(1, annotation.email());
        final UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(userInfo, "",
            List.of(new SimpleGrantedAuthority(annotation.role())));
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
