package com.nsfwllc.uberzettlegraphql;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities.DecodedId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecodeCursorTemplate implements GwtTemplate {
	protected final int                 maxPageSize       = 10;
	protected final int                 defaultPageSize   = 5;
	protected final String              type              = "type";
	protected final String              uuid              = "3b7dafc0-7437-4de7-a274-6fefac0ad266";
	protected       ControllerUtilities classUnderTest;
	protected       Optional<DecodedId> expectedDecodedId = Optional.empty();
	protected       Optional<DecodedId> actualDecodedId   = Optional.empty();
	protected       String              cursor            = "";

	@BeforeEach
	@Override
	public void given() {
		classUnderTest = new ControllerUtilities(defaultPageSize, maxPageSize);

	}

	@Override
	public void when() {
		actualDecodedId = classUnderTest.decodeCursor(cursor);
	}

	@AfterEach
	@Override
	public void then() {
		assertEquals(expectedDecodedId, actualDecodedId);
	}
}
