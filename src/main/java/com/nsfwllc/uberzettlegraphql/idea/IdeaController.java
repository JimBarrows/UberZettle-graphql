package com.nsfwllc.uberzettlegraphql.idea;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import graphql.relay.PageInfo;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Controller
public class IdeaController {

	private final IdeaRepository      ideaRepository;
	private final ControllerUtilities controllerUtil;

	public IdeaController(final IdeaRepository repository, final ControllerUtilities controllerUtil) {
		this.ideaRepository = repository;
		this.controllerUtil = controllerUtil;
	}

	@MutationMapping
	@Transactional
	public IdeaNode ideaCreate(@Argument IdeaCreateInput newIdea) {
		Idea idea = Idea.builder()
						.idea(newIdea.idea())
						.build();
		final var savedIdea = ideaRepository.save(idea);
		return new IdeaNode(controllerUtil.encodeCursor(Idea.class.getName(), savedIdea.getId())
		                                  .orElseThrow(() -> new RuntimeException("Could not encode cursor")),
		                    savedIdea.getIdea());
	}

	public record IdeaConnection(
			List<IdeaEdge> edges,
			PageInfo pageInfo
	) {
	}

	public record IdeaNode(String id, String idea) {}

	public record IdeaEdge(Idea node, String cursor) {}

	public record IdeaCreateInput(@NotEmpty String idea) {}
}
