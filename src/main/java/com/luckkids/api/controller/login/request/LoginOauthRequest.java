package com.luckkids.api.controller.login.request;

import lombok.Getter;

@Getter
public class LoginOauthRequest {

    private String code;
    private String authType;

}
