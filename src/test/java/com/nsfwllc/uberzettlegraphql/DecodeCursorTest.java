package com.nsfwllc.uberzettlegraphql;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities.DecodedId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

public class DecodeCursorTest extends DecodeCursorTemplate {

	@BeforeEach
	@Override
	public void given() {
		expectedDecodedId = Optional.of(new DecodedId(type, UUID.fromString(uuid)));
		cursor            = Base64.getUrlEncoder()
								  .withoutPadding()
								  .encodeToString((type + ":" + uuid)
														  .getBytes(StandardCharsets.UTF_8));
		super.given();
	}

	@Test
	@Override
	public void when() {
		super.when();
	}
}
