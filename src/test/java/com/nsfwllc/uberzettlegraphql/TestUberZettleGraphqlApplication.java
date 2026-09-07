package com.nsfwllc.uberzettlegraphql;

import org.springframework.boot.SpringApplication;

public class TestUberZettleGraphqlApplication {

	static void main(String[] args) {
		SpringApplication.from(UberZettleGraphqlApplication::main)
						 .with(TestcontainersConfiguration.class)
						 .run(args);
	}

}
