package com.nsfwllc.uberzettlegraphql;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("com.nsfwllc.uberzettlegraphql")
@ConfigurationParameters({
		// Targets the exact directory for your feature files
		@ConfigurationParameter(key = "cucumber.features", value = "src/behaviorTest/resources/features"),

		// Points Cucumber to the package where your Step Definitions are defined
		@ConfigurationParameter(key = "cucumber.glue", value = "com.nsfwllc.uberzettlegraphql")
})
public class RunCucumberTests {
}
