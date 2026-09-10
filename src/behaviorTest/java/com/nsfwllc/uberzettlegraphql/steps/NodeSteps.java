package com.nsfwllc.uberzettlegraphql.steps;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeSteps {
	private final IdeaRepository      ideaRepository;
	private final HttpGraphQlTester   httpGraphQlTester;
	private final ControllerUtilities controllerUtilities;
	private       Idea                expectedIdea = null;
	private       Response            actualResponse;
	private       Idea                actualIdea;
	private       IdeaNode            actualIdeaNode;
	private       String              encodedId;

	public NodeSteps(final IdeaRepository ideaRepository, final HttpGraphQlTester httpGraphQlTester,
					 final ControllerUtilities controllerUtilities) {
		this.ideaRepository      = ideaRepository;
		this.httpGraphQlTester   = httpGraphQlTester;
		this.controllerUtilities = controllerUtilities;
	}

	@Given("an idea exists in the repo")
	public void anIdeaExistsInTheRepo() {
		expectedIdea = ideaRepository.save(Idea.builder()
											   .idea("test")
											   .build());
		encodedId    = controllerUtilities
				.encodeCursor(Idea.class.getName(),
							  expectedIdea.getId())
				.orElse("");
	}

	@When("node is called with the idea id")
	public void nodeIsCalledWithTheIdeaId() {
		actualResponse = httpGraphQlTester.documentName("ideaById")
										  .variable("id", encodedId)
										  .execute();
		actualIdea     = actualResponse
				.path("data")
				.path("node")
				.entity(Idea.class)
				.get();
	}

	@Then("the idea is returned")
	public void theIdeaIsReturned() {
		Assertions.assertEquals(expectedIdea, actualIdea);
	}
}
