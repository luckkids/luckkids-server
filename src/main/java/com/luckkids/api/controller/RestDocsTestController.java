package com.luckkids.api.controller;

import com.luckkids.api.controller.dto.TestRequest;
import com.luckkids.api.controller.dto.TestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    @PostMapping("/restDocsTest")
    public TestResponse restDocsTestAPI(@RequestBody TestRequest testRequest) {
        TestResponse testResponse = new TestResponse();
        return testResponse;
    }
}
