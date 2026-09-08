package com.nsfwllc.uberzettlegraphql.steps;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.ControllerUtilities.DecodedId;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static graphql.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IdeaSteps {
	private final IdeaRepository      ideaRepository;
	private final HttpGraphQlTester   httpGraphQlTester;
	private final ControllerUtilities controllerUtilities;
	private       Idea                expectedIdea = null;
	private       Response            actualResponse;
	private       Idea                actualIdea;
	private       IdeaNode            actualIdeaNode;
	private       String              encodedId;
	private       Optional<DecodedId> actualId     = Optional.empty();

	public IdeaSteps(final IdeaRepository ideaRepository, final HttpGraphQlTester httpGraphQlTester,
					 final ControllerUtilities controllerUtilities) {
		this.ideaRepository      = ideaRepository;
		this.httpGraphQlTester   = httpGraphQlTester;
		this.controllerUtilities = controllerUtilities;
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
		if (actualResponse.returnResponse()
						  .getErrors()
						  .isEmpty()) {
			actualIdeaNode = actualResponse
					.path("data")
					.path("ideaCreate")
					.entity(IdeaNode.class)
					.get();
			actualId       = controllerUtilities.decodeCursor(actualIdeaNode.id());
		}
	}

	@Then("the idea is in the database")
	public void theIdeaIsInTheDatabase() {
		actualId.ifPresentOrElse(decodedId -> ideaRepository.findById(decodedId.id())
															.ifPresentOrElse(
																	idea -> assertEquals(expectedIdea.getIdea(),
																						 idea.getIdea()),
																	() -> fail("Idea " + decodedId + " not found")),
								 () -> fail("Could not decode " + actualIdeaNode.id()));
	}

	@Then("the idea is not in the database")
	public void theIdeaIsNotInTheDatabase() {
		assertFalse(ideaRepository.findAll()
								  .stream()
								  .anyMatch(idea -> expectedIdea.getIdea()
																.equals(idea.getIdea())));
	}

	@And("I have a must not be empty error message")
	public void iHaveAMustNotBeEmptyErrorMessage() {
		assertErrorMessagesThatSay(List.of("size must be between 1 and 500", "must not be empty"));
	}

	private void assertErrorMessagesThatSay(final List<String> expectedErrorMessages) {

		assertEquals(expectedErrorMessages.size(),
					 Stream.of(actualResponse.returnResponse()
											 .getErrors()
											 .getFirst()
											 .getMessage()
											 .split(","))
							 .map(String::trim)
						   .filter(expectedErrorMessages::contains)
						   .count(),
					 () -> "Expected error(s) message to be \"" + expectedErrorMessages + "\".  Error message(s): \n" +
						   actualResponse.returnResponse()
										 .getErrors()
										 .stream()
										 .map(ResponseError::getMessage)
										 .collect(Collectors.joining("\n")));
	}

	@And("I have a cannot exceed {int} character error message")
	public void iHaveACannotExceedCharacterErrorMessage(int arg0) {
		assertErrorMessagesThatSay(List.of("size must be between 1 and 500"));
	}
}
