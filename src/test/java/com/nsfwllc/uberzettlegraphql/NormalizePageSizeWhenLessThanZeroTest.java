package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.BeforeEach;

public class NormalizePageSizeWhenLessThanZeroTest extends NormalizePageSizeTest {

	@BeforeEach
	@Override
	public void given() {
		pageSize         = -1;
		expectedPageSize = defaultPageSize;
		super.given();
	}

}
