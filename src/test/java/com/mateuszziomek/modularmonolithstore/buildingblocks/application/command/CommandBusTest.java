package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommandBusTest {
    @Test
    void commandCanBeDispatched() {
        // Arrange
        var command = new TestCommand();
        var handler = new TestHandler();
        var sut = new CommandBus();
        sut.registerCommand(TestCommand.class, handler);

        // Act
        var result = sut.dispatch(command);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(handler.handleCount).isEqualTo(1);
    }

    @Test
    void commandWithoutHandlerCanNotBeDispatched() {
        // Arrange
        var command = new TestCommand();
        var sut = new CommandBus();

        // Act
        var result = sut.dispatch(command);

        // Assert
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void commandCanNotHaveMoreThanOneHandler() {
        // Arrange
        var handler = new TestHandler();
        var anotherHandler = new TestHandler();
        var sut = new CommandBus();
        sut.registerCommand(TestCommand.class, handler);

        // Act
        var result = sut.registerCommand(TestCommand.class, anotherHandler);

        // Assert
        assertThat(result.isFailure()).isTrue();
    }

    private static class TestCommand implements Command {}

    private static class TestHandler implements CommandHandler<TestCommand> {
        private int handleCount = 0;

        @Override
        public Try<Void> handle(TestCommand command) {
            handleCount += 1;

            return Try.success(null);
        }
    }
}