package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNull;

public class NormalizePageSizeWhenNullTest extends NormalizePageSizeTest {

	@BeforeEach
	@Override
	public void given() {
		pageSize = null;
		expectedPageSize = defaultPageSize;
		super.given();
	}

}
