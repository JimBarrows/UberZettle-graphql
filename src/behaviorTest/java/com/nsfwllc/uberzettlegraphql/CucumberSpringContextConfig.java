package com.nsfwllc.uberzettlegraphql;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@CucumberContextConfiguration
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = UberZettleGraphqlApplication.class
)
@Import(BehaviorTestConfig.class)
@ActiveProfiles("behavior")
public class CucumberSpringContextConfig {

}
