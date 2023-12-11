package com.luckkids.api.feign.apple;

import com.luckkids.api.feign.apple.request.AppleGetTokenRequest;
import com.luckkids.api.feign.apple.response.AppleTokenResponse;
import com.luckkids.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name="${oauth.apple.auth.name}", url="${oauth.apple.auth.url}", configuration = FeignConfig.class)
public interface AppleAuthFeignCall {

    @PostMapping(value="/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleTokenResponse getToken(@RequestBody AppleGetTokenRequest appleGetTokenRequest);

}
