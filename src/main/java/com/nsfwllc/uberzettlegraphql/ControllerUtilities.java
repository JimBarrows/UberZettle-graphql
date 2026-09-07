package com.nsfwllc.uberzettlegraphql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class ControllerUtilities {
	public final int defaultPageSize;
	public final int maxPageSize;

	public ControllerUtilities(
			@Value("${com.nsfwllc.uberzettlegraphql.controllers.defaultPageSize:20}") final int defaultPageSize,
			@Value("${com.nsfwllc.uberzettlegraphql.controllers.maxPageSize:100}") final int maxPageSize) {
		this.defaultPageSize = defaultPageSize;
		this.maxPageSize     = maxPageSize;
	}

	public int normalizePageSize(Integer first) {
		if (first == null || first < 1) {
			return defaultPageSize;
		}

		return Math.min(first, maxPageSize);
	}

	public Optional<DecodedId> decodeCursor(String cursor) {
		if (isEmpty(cursor)) {
			return empty();
		}

		String decoded = new String(
				Base64.getUrlDecoder()
					  .decode(cursor),
				StandardCharsets.UTF_8
		);
		String[] parts = decoded.split(":");
		if (parts.length != 2) {throw new IllegalArgumentException("Invalid cursor: " + cursor);}
		return Optional.of(new DecodedId(parts[0], UUID.fromString(parts[1])));
	}

	public String encodeCursor(String type, UUID id) {
		return Base64.getUrlEncoder()
					 .withoutPadding()
					 .encodeToString("""
									 ${type}:${id.toString()}
									 """
											 .getBytes(StandardCharsets.UTF_8));
	}

	public record DecodedId(String type, UUID id) {}
}
