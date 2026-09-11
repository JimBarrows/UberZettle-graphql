package com.nsfwllc.uberzettlegraphql.idea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class IdeaConnection extends DefaultConnection<IdeaNode> {

	@JsonCreator
	public IdeaConnection(
			@JsonProperty("edges") final List<IdeaEdge> edges,
			@JsonProperty("pageInfo") final IdeaPageInfo pageInfo
						 ) {
		super(edges == null
		      ? List.of()
		      : (List) edges,
			  pageInfo != null
		      ? pageInfo
		      : new DefaultPageInfo(null, null, false, false));
	}

	/**
	 * A connection consists of a list of edges and page info
	 *
	 * @param edges    a non null list of edges
	 * @param pageInfo a non null page info
	 * @throws IllegalArgumentException if edges or page info is null. use {@link Collections#emptyList()} for empty edges.
	 */
	public IdeaConnection(final List<Edge<IdeaNode>> edges, final PageInfo pageInfo) {
		super(edges, pageInfo);
	}
}
