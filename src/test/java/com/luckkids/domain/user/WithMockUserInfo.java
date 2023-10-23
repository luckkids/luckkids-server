package com.luckkids.domain.user;

import com.luckkids.WithMockUserInfoSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//userId도 필요하시면 추가하시면됩니다!!
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserInfoSecurityContextFactory.class)
public @interface WithMockUserInfo {
    String email() default "email@gamil.com";
    String role() default "USER";

}
