package com.nsfwllc.uberzettlegraphql;

import com.nsfwllc.uberzettlegraphql.idea.*;
import com.nsfwllc.uberzettlegraphql.idea.IdeaController.IdeaNode;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nsfwllc.uberzettlegraphql.ControllerUtilities.encodeCursor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class IdeaControllerIdeasTest implements GwtTemplate {

	@Mock
	protected static IdeaRepository       ideasRepository;
	protected static IdeaController       classUnderTest;
	private final AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
	protected        Integer              first;
	protected        Integer              last;
	protected        String               before;
	protected        String               after;
	protected        int                  defaultPageSize       = 10;
	protected        int                  maxPageSize           = 10;
	protected        IdeaConnection       actualIdeaConnection;
	protected        IdeaConnection       expectedIdeaConnection;
	protected        List<Edge<IdeaNode>> expectedIdeaEdges     = new ArrayList<>();
	protected        DefaultPageInfo      expectedPageInfo;
	protected        int                  expectedNumberOfIdeas = 5;
	protected        List<Idea>           expectedIdeas         = new ArrayList<>();

	@BeforeEach
	@Override
	public void given() {

		classUnderTest =
				new IdeaController(ideasRepository, new ControllerUtilities(defaultPageSize, maxPageSize));

		for (int i = 1; i <= expectedNumberOfIdeas; i++) {
			expectedIdeas.add(Idea.builder()
								  .id(UUID.randomUUID())
								  .idea("This is idea " + i)
								  .build());
		}
		Mockito.when(ideasRepository.findByOrderByIdAsc(any()))
			   .thenReturn(expectedIdeas);

		expectedIdeaEdges      = expectedIdeas.stream()
											  .<Edge<IdeaNode>>map(idea -> new IdeaEdge(
													  new IdeaNode(
															  encodeCursor(Idea.class.getName(), idea.getId())
																	  .orElse(""),
															  idea.getIdea()),
													  new DefaultConnectionCursor(
															  encodeCursor(Idea.class.getName(), idea.getId())
																	  .orElse(""))))
											  .toList();
		expectedPageInfo       = new DefaultPageInfo(expectedIdeaEdges.getFirst()
																	  .getCursor(),
													 expectedIdeaEdges.getLast()
																	  .getCursor(),
													 false,
													 false);
		expectedIdeaConnection = new IdeaConnection(expectedIdeaEdges, expectedPageInfo);
	}

	@Test
	@Override
	public void when() {
		actualIdeaConnection = classUnderTest.ideas(first, before, last, after);
	}

	@AfterEach
	@Override
	public void then() {
		assertEquals(expectedPageInfo, actualIdeaConnection.getPageInfo());
		assertEquals(expectedIdeaConnection.getEdges(), actualIdeaConnection.getEdges());
	}
}
