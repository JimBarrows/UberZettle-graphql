package com.nsfwllc.uberzettlegraphql.controllers;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
public class NodeController {

	private final IdeaRepository      ideaRepository;
	private final ControllerUtilities controllerUtil;

	public NodeController(final IdeaRepository ideaRepository, final ControllerUtilities controllerUtil) {
		this.ideaRepository = ideaRepository;
		this.controllerUtil = controllerUtil;
	}

	@QueryMapping
	public Object node(@Argument String id) {
		final var decodedCursor = controllerUtil.decodeCursor(id);
		return decodedCursor.map(decodedId ->
										 switch (decodedId.type()) {
											 case "com.nsfwllc.uberzettlegraphql.idea.Idea" -> ideaRepository.findById(decodedId.id());
											 default ->
													 throw new IllegalStateException("Unexpected value: " + decodedId);
										 })
							.orElse(null);
	}


}
