package com.luckkids.api.feign;

import com.luckkids.api.feign.request.KakaoGetTokenRequest;
import com.luckkids.api.feign.response.KakaoTokenResponse;
import com.luckkids.config.FeignConfig;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name="${oauth.kakao.name}", url="${oauth.kakao.url.auth}", configuration = FeignConfig.class)
public interface KakaoFeignCall {

    @PostMapping(value="/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public KakaoTokenResponse getToken(@RequestBody KakaoGetTokenRequest kakaoGetTokenRequest);

}

