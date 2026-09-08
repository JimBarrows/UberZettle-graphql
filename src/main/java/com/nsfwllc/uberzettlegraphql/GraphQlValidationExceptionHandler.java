package com.nsfwllc.uberzettlegraphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GraphQlValidationExceptionHandler extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
		if (ex instanceof ConstraintViolationException cve) {
			String errorMessage = cve.getConstraintViolations()
			                         .stream()
									 .map(ConstraintViolation::getMessage)
									 .collect(Collectors.joining(", "));

			return GraphqlErrorBuilder.newError(env)
									  .errorType(ErrorType.BAD_REQUEST)
									  .message(errorMessage)
									  .build();
		}
		return null;
	}
}
