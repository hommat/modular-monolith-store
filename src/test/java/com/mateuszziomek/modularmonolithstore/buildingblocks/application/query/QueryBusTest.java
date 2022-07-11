package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

class QueryBusTest {
    @Test
    void queryCanBeDispatched() {
        // Arrange
        var query = new TestQuery();
        var handler = new TestHandler();
        var sut = new QueryBus();
        sut.registerQuery(TestQuery.class, handler);

        // Act
        var result = sut.dispatch(query);

        // Assert
        StepVerifier
                .create(result)
                .expectNext("Some string")
                .verifyComplete();

        assertThat(handler.handleCount).isEqualTo(1);
    }

    @Test
    void queryCanNotHaveMoreThanOneHandler() {
        // Arrange
        var handler = new TestHandler();
        var anotherHandler = new TestHandler();
        var sut = new QueryBus();
        sut.registerQuery(TestQuery.class, handler);

        // Act
        var result = sut.registerQuery(TestQuery.class, anotherHandler);

        // Assert
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(QueryHandlerAlreadyRegisteredException.class);
    }

    private static class TestQuery implements Query<Mono<String>> {}

    private static class TestHandler implements QueryHandler<Mono<String>, TestQuery> {
        private int handleCount = 0;

        @Override
        public Mono<String> handle(TestQuery query) {
            handleCount += 1;

            return Mono.just("Some string");
        }
    }
}