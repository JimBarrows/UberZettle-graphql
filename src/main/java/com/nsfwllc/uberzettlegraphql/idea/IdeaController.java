package com.nsfwllc.uberzettlegraphql.idea;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import graphql.relay.PageInfo;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Controller
public class IdeaController {

	private final IdeaRepository      ideaRepository;
	private final ControllerUtilities controllerUtil;

	public IdeaController(final IdeaRepository repository, final ControllerUtilities controllerUtil) {
		this.ideaRepository = repository;
		this.controllerUtil = controllerUtil;
	}

	@QueryMapping
	public Idea idea(@Argument UUID id) {
		return ideaRepository.findById(id)
							 .orElseThrow(() -> new IllegalArgumentException("Idea not found: " + id));
	}

	@MutationMapping
	@Transactional
	public Idea addIdea(@Argument CreateIdeaInput newIdea) {
		Idea idea = Idea.builder()
						.build();
		return ideaRepository.save(idea);
	}

	@MutationMapping
	@Transactional
	public Idea updateIdea(@Argument UUID id, @Argument UpdateIdeaInput updateIdea) {
		Idea idea = ideaRepository.findById(id)
								  .orElseThrow(() -> new IllegalArgumentException("Idea not found: " + id));

		Idea updated = Idea.builder()
						   .build();

		return ideaRepository.save(updated);
	}

	@MutationMapping
	@Transactional
	public DeleteIdeaOutput deleteIdea(@Argument UUID id) {
		if (!ideaRepository.existsById(id)) {
			return new DeleteIdeaOutput(id, false);
		}

		ideaRepository.deleteById(id);

		return new DeleteIdeaOutput(id, true);
	}

	public record IdeaConnection(
			List<IdeaEdge> edges,
			PageInfo pageInfo
	) {
	}

	public record IdeaEdge(Idea node, String cursor) {
	}

	public record CreateIdeaInput() {
	}

	public record UpdateIdeaInput() {
	}

	public record DeleteIdeaOutput(UUID id, boolean deleted) {
	}
}
