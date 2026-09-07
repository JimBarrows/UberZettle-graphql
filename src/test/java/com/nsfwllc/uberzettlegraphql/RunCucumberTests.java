package com.nsfwllc.uberzettlegraphql;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
public class RunCucumberTests {
}
