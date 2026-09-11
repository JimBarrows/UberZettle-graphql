package com.nsfwllc.uberzettlegraphql.idea;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.nsfwllc.uberzettlegraphql.ControllerUtilities.encodeCursor;


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
	public IdeaConnection ideas(@Argument Integer first, @Argument String after, @Argument Integer last,
								@Argument String before) {
		PageRequest page = PageRequest.of(0, 1000);
		final var edgeList = ideaRepository.findByOrderByIdAsc(page)
										   .stream()
										   .<Edge<IdeaNode>>map(idea -> new IdeaEdge(
												   new IdeaNode(
														   encodeCursor(Idea.class.getName(), idea.getId()).orElse(
																   ""),
														   idea.getIdea()),
												   new DefaultConnectionCursor(
														   encodeCursor(Idea.class.getName(), idea.getId()).orElse(
																   ""))))
										   .toList();
		final boolean hasPreviousPage = false;
		final boolean hasNextPage     = false;
		final var firstCursor = new DefaultConnectionCursor(edgeList.stream()
																	.findFirst()
																	.orElse(new IdeaEdge(null,
																						 new DefaultConnectionCursor(
																								 "There is no cursor")))
																	.getCursor()
																	.getValue());
		final var lastCursor = new DefaultConnectionCursor(edgeList.stream()
																   .reduce((firstEdge, secondEdge) -> secondEdge)
																   .orElse(new IdeaEdge(null,
																						new DefaultConnectionCursor(
																								"There is no cursor")))
																   .getCursor()
																   .getValue());
		return new IdeaConnection(edgeList,
								  new DefaultPageInfo(
										  firstCursor,
										  lastCursor,
										  hasPreviousPage,
										  hasNextPage)
		);
	}

	public record IdeaNode(String id, String idea) {}


	public record IdeaCreateInput(@NotEmpty @Size(min = 1, max = 500) String idea) {}

}
