package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.BeforeEach;

public class NormalizePageSizeWhenGreaterThanMaxTest extends NormalizePageSizeTest {

	@BeforeEach
	@Override
	public void given() {
		pageSize         = maxPageSize + 10;
		expectedPageSize = maxPageSize;
		super.given();
	}

}
