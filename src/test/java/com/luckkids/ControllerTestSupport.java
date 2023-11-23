package com.luckkids;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckkids.api.controller.alertSetting.AlertSettingController;
import com.luckkids.api.controller.friend.FriendController;
import com.luckkids.api.ErrorNotifier;
import com.luckkids.api.controller.join.JoinController;
import com.luckkids.api.controller.login.LoginController;
import com.luckkids.api.controller.mail.MailController;
import com.luckkids.api.controller.mission.MissionController;
import com.luckkids.api.controller.missionOutcome.MissionOutcomeController;
import com.luckkids.api.controller.notice.NoticeController;
import com.luckkids.api.controller.user.UserController;
import com.luckkids.api.controller.version.VersionController;
import com.luckkids.api.service.alertSetting.AlertSettingReadService;
import com.luckkids.api.service.alertSetting.AlertSettingService;
import com.luckkids.api.service.friend.FriendReadService;
import com.luckkids.api.service.join.JoinReadService;
import com.luckkids.api.service.join.JoinService;
import com.luckkids.api.service.login.LoginService;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.mission.MissionReadService;
import com.luckkids.api.service.mission.MissionService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeReadService;
import com.luckkids.api.service.missionOutcome.MissionOutcomeService;
import com.luckkids.api.service.notice.NoticeReadService;
import com.luckkids.api.service.notice.NoticeService;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.api.service.user.UserService;
import com.luckkids.api.service.version.VersionReadService;
import com.luckkids.api.service.version.VersionService;
import org.mockito.Mock;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    MissionController.class,
    LoginController.class,
    JoinController.class,
    MailController.class,
    MissionOutcomeController.class,
    VersionController.class,
    NoticeController.class,
    FriendController.class,
    AlertSettingController.class,
    UserController.class
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
    protected FriendReadService friendReadService;

    @MockBean
    protected MissionOutcomeService missionOutcomeService;

    @MockBean
    protected VersionReadService versionReadService;

    @MockBean
    protected VersionService versionService;

    @MockBean
    protected NoticeReadService noticeReadService;

    @MockBean
    protected NoticeService noticeService;
  
    @MockBean
    protected EntityManager entityManager;

    @MockBean
    protected MissionOutcomeReadService missionOutcomeReadService;

    @MockBean
    protected AlertSettingReadService alertSettingReadService;

    @MockBean
    protected AlertSettingService alertSettingService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected ErrorNotifier errorNotifier;
}

