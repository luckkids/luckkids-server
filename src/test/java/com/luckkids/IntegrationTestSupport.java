package com.luckkids;

import com.amazonaws.services.s3.AmazonS3Client;
import com.luckkids.api.service.mail.MailService;
import com.luckkids.api.service.s3.S3Service;
import com.luckkids.api.service.security.SecurityService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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

    @MockBean
    protected AmazonS3Client amazonS3Client;
}
