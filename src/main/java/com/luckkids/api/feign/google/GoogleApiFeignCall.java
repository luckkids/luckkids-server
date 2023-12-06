package com.luckkids.api.feign.google;

import com.luckkids.api.feign.google.response.GoogleUserInfoResponse;
import com.luckkids.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name="${oauth.google.name.api}", url="${oauth.google.url.api}", configuration = FeignConfig.class)
public interface GoogleApiFeignCall {

    @GetMapping(value = "/oauth2/v1/userinfo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleUserInfoResponse getUserInfo(@RequestHeader("Authorization") String auth);
}
