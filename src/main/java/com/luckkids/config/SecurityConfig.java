package com.luckkids.config;

import com.luckkids.jwt.JwtTokenProvider;
import com.luckkids.jwt.filter.JwtAuthenticationFilter;
import com.luckkids.jwt.filter.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                    new AntPathRequestMatcher("/**"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/h2-console/**"),
                    new AntPathRequestMatcher("/health-check")
                ).permitAll()
                .anyRequest().authenticated())

            .logout((logout) -> logout
                .logoutSuccessUrl("/"))

            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class); //JwtAuthenticationFilter에서 뱉은 에러를 처리하기 위한 Filter

        return http.build();
    }
}

