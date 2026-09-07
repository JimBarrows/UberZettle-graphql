package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.BeforeEach;

public class NormalizePageSizeWhenNullTest extends NormalizePageSizeTest {

	@BeforeEach
	@Override
	public void given() {
		pageSize         = null;
		expectedPageSize = defaultPageSize;
		super.given();
	}

}
