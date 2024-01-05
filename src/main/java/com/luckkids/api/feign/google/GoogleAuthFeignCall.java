package com.luckkids.api.feign.google;

import com.luckkids.api.feign.google.request.GoogleGetTokenRequest;
import com.luckkids.api.feign.google.response.GoogleTokenResponse;
import com.luckkids.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@Component
//@FeignClient(name="${oauth.google.api.name}", url="${oauth.google.api.url}", configuration = FeignConfig.class)
public interface GoogleAuthFeignCall {
    @PostMapping(value="/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse getToken(@RequestBody GoogleGetTokenRequest googleGetTokenRequest);
}
