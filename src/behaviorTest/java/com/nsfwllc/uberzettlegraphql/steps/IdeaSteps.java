package com.nsfwllc.uberzettlegraphql.steps;

import com.nsfwllc.uberzettlegraphql.ControllerUtilities;
import com.nsfwllc.uberzettlegraphql.ControllerUtilities.DecodedId;
import com.nsfwllc.uberzettlegraphql.idea.Idea;
import com.nsfwllc.uberzettlegraphql.idea.IdeaConnection;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import com.nsfwllc.uberzettlegraphql.idea.IdeaRepository;
import graphql.Assert;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nsfwllc.uberzettlegraphql.ControllerUtilities.decodeCursor;
import static com.nsfwllc.uberzettlegraphql.ControllerUtilities.encodeCursor;
import static graphql.Assert.assertFalse;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdeaSteps {
	private final IdeaRepository      ideaRepository;
	private final HttpGraphQlTester   httpGraphQlTester;
	private final ControllerUtilities controllerUtilities;
	IdeaConnection actualIdeaConnection = null;
	private Idea                expectedIdea  = null;
	private Response            actualResponse;
	private IdeaNode            actualIdeaNode;
	private Optional<DecodedId> actualId      = empty();
	private List<Idea>          expectedIdeas = new ArrayList<>();

	public IdeaSteps(final IdeaRepository ideaRepository, final HttpGraphQlTester httpGraphQlTester,
					 final ControllerUtilities controllerUtilities) {
		this.ideaRepository      = ideaRepository;
		this.httpGraphQlTester   = httpGraphQlTester;
		this.controllerUtilities = controllerUtilities;
	}

	@Before
	public void cleanDatabase() {
		ideaRepository.deleteAll();
		expectedIdea         = null;
		actualResponse       = null;
		actualIdeaNode       = null;
		actualId             = empty();
		actualIdeaConnection = null;
		expectedIdeas        = new ArrayList<>();
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
			actualId       = ControllerUtilities.decodeCursor(actualIdeaNode.id());
		}
	}

	@Then("the idea is in the database")
	public void theIdeaIsInTheDatabase() {
		actualId.ifPresentOrElse(decodedId -> ideaRepository.findById(decodedId.id())
															.ifPresentOrElse(
																	idea -> assertEquals(
																			expectedIdea.getIdea(),
																			idea.getIdea()),
																	() -> Assertions.fail(
																			"Idea " + decodedId + " not found")),
								 () -> Assertions.fail("Could not decode " + actualIdeaNode.id()));
	}

	@Then("the idea is not in the database")
	public void theIdeaIsNotInTheDatabase() {
		Assert.assertFalse(ideaRepository.findAll()
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
					 () -> "Expected error(s) message to be \"" + expectedErrorMessages +
						   "\".  Error message(s): \n" +
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

	@Given("there are {int} ideas in the database")
	public void thereAreIdeasInTheDatabase(int numberOfIdeas) {
		for (int i = 0; i < numberOfIdeas; i++) {
			expectedIdeas.add(Idea.builder()
								  .idea("This is idea " + i)
								  .build());
		}
		expectedIdeas = ideaRepository.saveAll(expectedIdeas);
		expectedIdeas = ideaRepository.findByOrderByIdAsc(Pageable.unpaged());
	}

	@When("I query for a list")
	public void iQueryForAList() {
		actualResponse       = httpGraphQlTester.documentName("ideas")
												.execute();
		actualIdeaConnection = actualResponse
				.path("data")
				.path("ideas")
				.entity(IdeaConnection.class)
				.get();
	}

	@Then("I get {int} ideas")
	public void iGetIdeas(int ideaCount) {
		assertEquals(ideaCount, actualIdeaConnection.getEdges()
													.size());
		final var ideaList = actualIdeaConnection.getEdges()
												 .stream()
												 .map(edge -> edge.getNode()
																  .idea())
												 .toList();
		assertEquals(ideaCount, expectedIdeas
				.stream()
				.filter(idea ->
								ideaList.contains(idea.getIdea()))
				.count());

	}

	@Then("I am on page {int}")
	public void iAmOnPage(int pageNumber) {
		if (pageNumber == 1) {
			assertTrue(actualIdeaConnection.getPageInfo()
										   .isHasNextPage(), "There should be a next page");
			assertFalse(actualIdeaConnection.getPageInfo()
											.isHasPreviousPage(), "There should not be previous page");
		} else {
			assertTrue(actualIdeaConnection.getPageInfo()
										   .isHasNextPage(), "There should be a next page");
			assertTrue(actualIdeaConnection.getPageInfo()
										   .isHasPreviousPage(), "There should be previous page");
		}

	}

	@When("I query for the next page")
	public void iQueryForTheNextPage() {
		actualResponse       = httpGraphQlTester.documentName("ideas")
												.variable("after", actualIdeaConnection.getPageInfo()
																					   .getEndCursor()
																					   .getValue())
												.execute();
		actualIdeaConnection = actualResponse
				.path("data")
				.path("ideas")
				.entity(IdeaConnection.class)
				.get();
	}

	@When("I query for the previous page")
	public void iQueryForThePreviousPage() {
		actualResponse       = httpGraphQlTester.documentName("ideas")
												.variable("before", actualIdeaConnection.getPageInfo()
																						.getEndCursor()
																						.getValue())
												.execute();
		actualIdeaConnection = actualResponse
				.path("data")
				.path("ideas")
				.entity(IdeaConnection.class)
				.get();
	}

	@When("I query for the first {int} from the index of {int}")
	public void iQueryForFromTheIndexOf(int count, int index) {
		Optional<String> cursor;
		if ((index == 0) || (index >= expectedIdeas.size())) {
			cursor = empty();
		} else {
			cursor = encodeCursor(Idea.class.getName(), expectedIdeas.get(index)
																	 .getId());
		}
		actualResponse       = httpGraphQlTester.documentName("ideas")
												.variable("first", count)
												.variable("after", cursor.orElse(null))
												.execute();
		actualIdeaConnection = actualResponse
				.path("data")
				.path("ideas")
				.entity(IdeaConnection.class)
				.get();
	}


	@And("I have {int} items in the list")
	public void iHaveItemsInTheList(int count) {
		assertEquals(count, actualIdeaConnection.getEdges()
												.size());
	}

	@And("the first item in the list is the same as the first idea")
	public void theFirstItemInTheListIsTheSameAsTheFirstIdea() {
		assertEquals(expectedIdeas.getFirst()
								  .getId(),
					 decodeCursor(actualIdeaConnection.getEdges()
													  .getFirst()
													  .getCursor()
													  .getValue())
							 .orElse(new DecodedId(null, null))
							 .id());
	}

	@And("the last item in the list is the same as the {int} idea in the list")
	public void theLastItemInTheListIsTheSameAsTheIdeaInTheList(int index) {
		assertEquals(expectedIdeas.get(index)
								  .getId(),
					 decodeCursor(actualIdeaConnection.getEdges()
													  .getLast()
													  .getCursor()
													  .getValue())
							 .orElse(new DecodedId(null, null))
							 .id());
	}

	@When("I query for the last {int} from the index of {int}")
	public void iQueryForTheLastFromTheIndexOf(int count, int index) {
		Optional<String> cursor;
		if ((index == 0) || (index >= expectedIdeas.size())) {
			cursor = empty();
		} else {
			cursor = encodeCursor(Idea.class.getName(), expectedIdeas.get(index)
																	 .getId());
		}
		actualResponse       = httpGraphQlTester.documentName("ideas")
												.variable("last", count)
												.variable("before", cursor.orElse(null))
												.execute();
		actualIdeaConnection = actualResponse
				.path("data")
				.path("ideas")
				.entity(IdeaConnection.class)
				.get();
	}

	@And("the first item in the list is the same as the {int} idea")
	public void theFirstItemInTheListIsTheSameAsTheIdea(int index) {
		final AtomicInteger i = new AtomicInteger();
		ideaRepository.findByOrderByIdDesc(Pageable.unpaged()).subList(0, 10)
					 .forEach(idea -> System.out.println("expected [" + i.getAndIncrement() + "]: " + idea.getId() + " = " + idea.getIdea()));
		i.set(0);
		actualIdeaConnection.getEdges()
		                    .forEach(edge -> System.out.println("actual:  [" + i.getAndIncrement() + "]: "
																+ decodeCursor(edge.getCursor().getValue()).get().id()
																+ " = "
																+ edge.getNode().idea()));
		assertEquals(expectedIdeas.get(index)
								  .getId(),
					 decodeCursor(actualIdeaConnection.getEdges()
													  .getFirst()
													  .getCursor()
													  .getValue())
							 .orElse(new DecodedId(null, null))
							 .id());
	}
}
