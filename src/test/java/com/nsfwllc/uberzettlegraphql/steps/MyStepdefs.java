package com.nsfwllc.uberzettlegraphql.steps;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {
	private final IdeaRepository ideaRepository;
	String document =
			"""
			query GetNode($id: ID!) {
			    node(id: $id) {
			      id
			      ... on Idea {
			        idea
			      }
			    }
			  }
			""";
	private HttpGraphQlTester   httpGraphQlTester;
	private ControllerUtilities controllerUtilities;
	private Idea                expectedIdea = null;
	private Response            actualResponse;
	private Idea                actualIdea;
	private String              encodedId;

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
		actualResponse = httpGraphQlTester.document(document)
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
}
