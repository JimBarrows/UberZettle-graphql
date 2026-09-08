package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalizePageSizeTest implements GwtTemplate {

	protected final int                 maxPageSize      = 10;
	protected final int                 defaultPageSize  = 5;
	protected       ControllerUtilities classUnderTest;
	protected       Integer             expectedPageSize = 10;
	protected       Integer             pageSize         = 10;
	protected       int                 actualPageSize;

	@BeforeEach
	@Override
	public void given() {
		classUnderTest = new ControllerUtilities(defaultPageSize, maxPageSize);
	}

	@Test
	@Override
	public void when() {
		actualPageSize = classUnderTest.normalizePageSize(pageSize);
	}

	@AfterEach
	@Override
	public void then() {
		assertEquals(expectedPageSize, actualPageSize);
	}
}
