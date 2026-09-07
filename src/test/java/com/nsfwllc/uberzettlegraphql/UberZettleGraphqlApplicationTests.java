package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UberZettleGraphqlApplicationTests {

	@Test
	void contextLoads() {
	}

}
