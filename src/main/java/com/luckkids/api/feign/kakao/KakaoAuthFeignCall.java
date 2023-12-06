package com.luckkids.api.feign.kakao;

import com.luckkids.api.feign.kakao.request.KakaoGetTokenRequest;
import com.luckkids.api.feign.kakao.response.KakaoTokenResponse;
import com.luckkids.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name="${oauth.kakao.name.auth}", url="${oauth.kakao.url.auth}", configuration = FeignConfig.class)
public interface KakaoAuthFeignCall {

    @PostMapping(value="/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse getToken(@RequestBody KakaoGetTokenRequest kakaoGetTokenRequest);

}

