package com.nsfwllc.uberzettlegraphql;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@CucumberContextConfiguration
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = SpringTestConfig.class
)
@ActiveProfiles("test")
public class CucumberSpringContextConfig {
	// Define the container as a static singleton instance
	static final PostgreSQLContainer postgresContainer =
			new PostgreSQLContainer("postgres:16-alpine");

	static {
		// Explicitly start the container before Spring context initializes
		postgresContainer.start();
	}

	// Dynamic database credentials supplied into the Spring Context
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

}
