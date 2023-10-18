package com.luckkids;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"local", "test"})
@SpringBootTest
public abstract class IntegrationTestSupport {
}
