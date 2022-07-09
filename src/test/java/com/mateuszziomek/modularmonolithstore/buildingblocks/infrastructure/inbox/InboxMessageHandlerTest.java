package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;

import static org.assertj.core.api.Assertions.*;

class InboxMessageHandlerTest {
    @Test
    void messageCanBeProcessedOnce() {
        // Arrange
        var inboxRepository = new InMemoryInboxMessageRepository();
        var message = new TestIntegrationMessage();
        var handler = new TestIntegrationMessageHandler();
        var inboxHandler = new InboxMessageHandler<TestIntegrationMessage>(inboxRepository, handler);

        // Act
        var result = inboxHandler.handle(message);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(handler.processCount).isEqualTo(1);
    }

    @Test
    void messageCanNotBeProcessedTwice() {
        // Arrange
        var inboxRepository = new InMemoryInboxMessageRepository();
        var message = new TestIntegrationMessage();
        var handler = new TestIntegrationMessageHandler();
        var inboxHandler = new InboxMessageHandler<TestIntegrationMessage>(inboxRepository, handler);
        inboxHandler.handle(message);

        // Act
        var result = inboxHandler.handle(message);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(handler.processCount).isEqualTo(1);
    }

    private static class TestIntegrationMessage extends IntegrationMessage {}

    private static class TestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        public int processCount = 0;

        @Override
        public Try<Void> handle(final TestIntegrationMessage event) {
            Preconditions.notNull(event, "Event can't be null");

            processCount += 1;
            return Try.success(null);
        }
    }
}