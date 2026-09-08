package com.nsfwllc.uberzettlegraphql.steps;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MyStepdefs {
	private final IdeaRepository      ideaRepository;
	private final HttpGraphQlTester   httpGraphQlTester;
	private final ControllerUtilities controllerUtilities;
	private       Idea                expectedIdea = null;
	private       Response            actualResponse;
	private       Idea                actualIdea;
	private       IdeaNode            actualIdeaNode;
	private       String              encodedId;

	public MyStepdefs(final IdeaRepository ideaRepository, final HttpGraphQlTester httpGraphQlTester,
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
		assertEquals(expectedIdea, actualIdea);
	}

	@Given("an idea of {string}")
	public void anIdeaOf(String idea) {
		if (expectedIdea == null) {
			expectedIdea = new Idea();
		}
		expectedIdea.setIdea(idea);
	}

	@When("I create the idea")
	public void iCreateTheIdea() {
		actualResponse = httpGraphQlTester.documentName("ideaCreate")
										  .variable("newIdea", Map.of(
												  "idea", expectedIdea.getIdea()))
										  .execute();
		actualIdeaNode = actualResponse
				.path("data")
				.path("ideaCreate")
				.entity(IdeaNode.class)
				.get();
	}

	@Then("the idea is in the database")
	public void theIdeaIsInTheDatabase() {
		var id = controllerUtilities.decodeCursor(actualIdeaNode.id());

		id.ifPresentOrElse(decodedId -> {
							   ideaRepository.findById(decodedId.id())
											 .ifPresentOrElse(
													 idea -> assertEquals(expectedIdea.getIdea(), idea.getIdea()),
													 () -> fail("Idea " + decodedId + " not found"));
						   },
						   () -> fail("Could not decode " + actualIdeaNode.id()));
	}
}
