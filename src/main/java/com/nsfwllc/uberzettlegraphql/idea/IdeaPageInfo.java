package com.nsfwllc.uberzettlegraphql.idea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.PageInfo;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class IdeaPageInfo implements PageInfo {
	private final ConnectionCursor startCursor;
	private final ConnectionCursor endCursor;
	private final boolean          hasPreviousPage;
	private final boolean          hasNextPage;

	@JsonCreator
	public IdeaPageInfo(
			@JsonProperty("startCursor") final String startCursor,
			@JsonProperty("endCursor") final String endCursor,
			@JsonProperty("hasPreviousPage") final boolean hasPreviousPage,
			@JsonProperty("hasNextPage") final boolean hasNextPage
					   ) {
		this.startCursor     = startCursor != null
		                       ? new DefaultConnectionCursor(startCursor)
		                       : null;
		this.endCursor       = endCursor != null
		                       ? new DefaultConnectionCursor(endCursor)
		                       : null;
		this.hasPreviousPage = hasPreviousPage;
		this.hasNextPage     = hasNextPage;
	}

	public IdeaPageInfo(final ConnectionCursor startCursor, final ConnectionCursor endCursor,
						final boolean hasPreviousPage, final boolean hasNextPage) {
		this.startCursor     = startCursor;
		this.endCursor       = endCursor;
		this.hasPreviousPage = hasPreviousPage;
		this.hasNextPage     = hasNextPage;
	}

	@Override
	public ConnectionCursor getStartCursor() {
		return startCursor;
	}

	@Override
	public ConnectionCursor getEndCursor() {
		return endCursor;
	}

	@Override
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	@Override
	public boolean isHasNextPage() {
		return hasNextPage;
	}
}
