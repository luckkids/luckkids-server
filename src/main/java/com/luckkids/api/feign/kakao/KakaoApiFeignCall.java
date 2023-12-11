package com.luckkids.api.feign.kakao;

import com.luckkids.api.feign.kakao.response.KakaoUserInfoResponse;
import com.luckkids.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name="${oauth.kakao.api.name}", url="${oauth.kakao.api.url}", configuration = FeignConfig.class)
public interface KakaoApiFeignCall {

    @PostMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserInfoResponse getUserInfo(@RequestHeader("Authorization") String auth);

}
