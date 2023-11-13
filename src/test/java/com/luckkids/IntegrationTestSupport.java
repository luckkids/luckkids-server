package com.luckkids;

import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.security.SecurityService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockBean
    protected SecurityService securityService;

    @MockBean
    protected MailService mailService;
}
