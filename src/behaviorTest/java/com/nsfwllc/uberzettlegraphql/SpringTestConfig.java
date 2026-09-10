package com.nsfwllc.uberzettlegraphql;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Lazy
@TestConfiguration
public class SpringTestConfig {
	@LocalServerPort
	private int port;
	@Bean
	public HttpGraphQlTester httpGraphQlTester() {
		WebTestClient client =
				WebTestClient.bindToServer()
							 .baseUrl("http://localhost:" + port + "/graphql")
							 .build();

		return HttpGraphQlTester.create(client);
	}

}
