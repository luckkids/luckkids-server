package com.luckkids.global.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @description 
 * BasicEntity에서 @CreatedBy @LastModifiedBy의 자동으로 기입되게 하는 Audit설정
 * 일단 Session에서 가져오는 걸로 설정해둠 추후에 Accesstoken에서 사용자정보 가져오는 걸로 수정 할 예정
 * @author skhan
 * */
@Configuration
public class AuditConfig implements AuditorAware<String>{
	@Override
	public Optional<String> getCurrentAuditor() {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = httpServletRequest.getSession();
		String userId = (String) session.getAttribute("userId");
		if(userId==null) {
			userId = "admin";
		}
		return Optional.of(userId);
	}
}