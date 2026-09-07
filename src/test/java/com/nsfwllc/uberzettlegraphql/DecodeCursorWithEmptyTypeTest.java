package com.nsfwllc.uberzettlegraphql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class DecodeCursorWithEmptyTypeTest extends DecodeCursorTest {
	@BeforeEach
	@Override
	public void given() {
		cursor            = "";
		expectedDecodedId = Optional.empty();
		super.given();
	}

	@Test
	@Override
	public void when() {
		super.when();
	}
}
