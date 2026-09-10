package com.nsfwllc.uberzettlegraphql;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class BehaviorTestConfig {
	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		return new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));
	}

	@Bean
	@Lazy
	HttpGraphQlTester httpGraphQlTester(@LocalServerPort int port) {
		WebTestClient client =
				WebTestClient.bindToServer()
							 .baseUrl("http://localhost:" + port + "/graphql")
							 .build();

		return HttpGraphQlTester.create(client);
	}
}
