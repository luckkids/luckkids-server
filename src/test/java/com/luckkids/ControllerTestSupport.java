package com.luckkids;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.controller.login.LoginController;
import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.mission.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    MissionController.class,
    LoginController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MissionService missionService;

    @MockBean
    protected LoginService loginService;


}

