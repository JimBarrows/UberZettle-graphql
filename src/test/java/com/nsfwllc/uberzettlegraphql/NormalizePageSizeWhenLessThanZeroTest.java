package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNull;

public class NormalizePageSizeWhenLessThanZeroTest extends NormalizePageSizeTest {

	@BeforeEach
	@Override
	public void given() {
		pageSize = -1;
		expectedPageSize = defaultPageSize;
		super.given();
	}

}
