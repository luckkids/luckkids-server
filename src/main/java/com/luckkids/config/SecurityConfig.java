package com.luckkids.config;

import com.luckkids.jwt.JwtTokenProvider;
import com.luckkids.jwt.filter.JwtAuthenticationFilter;
import com.luckkids.jwt.filter.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/images/**"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/health-check")
                ).permitAll()
                .anyRequest().authenticated()
            )

            .logout((logout) -> logout
                    .logoutSuccessUrl("/"))

            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class); //JwtAuthenticationFilter에서 뱉은 에러를 처리하기 위한 Filter

        return http.build();
    }

    /*
    * JwtFilter 제외 API 지정
    * permitAll이 모든 요청 처리 과정에서 적용되는 모든 필터들을 무시하지않는다고 한다.
    * 참고: https://velog.io/@refoli_20/Spring-Security-permitAll-Filter-%ED%98%B8%EC%B6%9C
    * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/api/v1/jwt/**",   //jwt토큰 발급 테스트 API
                        "/api/v1/join/**",  //회원가입 API
                        "/api/v1/auth/**"   //인증 API
                );
    }
}

