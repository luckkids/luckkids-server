package com.luckkids;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.controller.join.JoinController;
import com.luckkids.api.controller.login.LoginController;
import com.luckkids.api.controller.mail.MailController;
import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.controller.missionOutcome.MissionOutcomeController;
import com.luckkids.api.service.join.JoinReadService;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    MissionController.class,
    LoginController.class,
    JoinController.class,
    MailController.class,
    MissionOutcomeController.class
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

    @MockBean
    protected MissionReadService missionReadService;

    @MockBean
    protected JoinService joinService;

    @MockBean
    protected JoinReadService joinReadService;

    @MockBean
    protected MailService mailService;

    @MockBean
    protected SecurityService securityService;

    @MockBean
    protected MissionOutcomeService missionOutcomeService;
}

