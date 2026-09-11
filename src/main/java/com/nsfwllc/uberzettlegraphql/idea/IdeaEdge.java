package com.nsfwllc.uberzettlegraphql.idea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class IdeaEdge implements Edge<IdeaNode> {
	private final IdeaNode         node;
	private final ConnectionCursor cursor;

	@JsonCreator
	public IdeaEdge(
			@JsonProperty("node") final IdeaNode node,
			@JsonProperty("cursor") final String cursor
				   ) {
		this.node   = node;
		this.cursor = cursor != null
		              ? new DefaultConnectionCursor(cursor)
		              : null;
	}

	public IdeaEdge(final IdeaNode node, final ConnectionCursor cursor) {
		this.node   = node;
		this.cursor = cursor;
	}

	/**
	 * @return the node of data that this edge represents
	 */
	@Override
	public IdeaNode getNode() {
		return node;
	}

	/**
	 * @return the cursor for this edge node
	 */
	@Override
	public ConnectionCursor getCursor() {
		return cursor;
	}
}
