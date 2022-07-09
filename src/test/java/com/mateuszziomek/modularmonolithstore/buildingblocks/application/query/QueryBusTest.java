package com.mateuszziomek.modularmonolithstore.buildingblocks.application.query;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

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
        assertThat(result.isSuccess()).isTrue();
        assertThat(handler.handleCount).isEqualTo(1);
    }

    @Test
    void queryWithoutHandlerCanNotBeDispatched() {
        // Arrange
        var command = new TestQuery();
        var sut = new QueryBus();

        // Act
        var result = sut.dispatch(command);

        // Assert
        assertThat(result.isFailure()).isTrue();
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
    }

    private static class TestQuery implements Query<String> {}

    private static class TestHandler implements QueryHandler<String, TestQuery> {
        private int handleCount = 0;

        @Override
        public Try<String> handle(TestQuery query) {
            handleCount += 1;

            return Try.success(null);
        }
    }
}