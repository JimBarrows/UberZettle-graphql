package com.nsfwllc.uberzettlegraphql.idea;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Controller
@Validated
public class IdeaController {

	private final IdeaRepository      ideaRepository;
	private final ControllerUtilities controllerUtil;

	public IdeaController(final IdeaRepository repository, final ControllerUtilities controllerUtil) {
		this.ideaRepository = repository;
		this.controllerUtil = controllerUtil;
	}

	@MutationMapping
	@Transactional
	public IdeaNode ideaCreate(@Argument @Valid IdeaCreateInput newIdea) {
		Idea idea = Idea.builder()
						.idea(newIdea.idea())
						.build();
		final var savedIdea = ideaRepository.save(idea);
		return new IdeaNode(controllerUtil.encodeCursor(Idea.class.getName(), savedIdea.getId())
										  .orElseThrow(() -> new RuntimeException("Could not encode cursor")),
							savedIdea.getIdea());
	}

	@QueryMapping
	public IdeaConnection ideas(@Argument int first, @Argument String after, @Argument int last,
	                            @Argument String before) {
		return null;
	}

	public record IdeaNode(String id, String idea) {}

	public static class IdeaEdge implements Edge<IdeaNode> {
		private final IdeaNode         node;
		private final ConnectionCursor cursor;

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

	public record IdeaCreateInput(@NotEmpty @Size(min = 1, max = 500) String idea) {}

	public static class IdeaConnection extends DefaultConnection<IdeaNode> {

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
}
