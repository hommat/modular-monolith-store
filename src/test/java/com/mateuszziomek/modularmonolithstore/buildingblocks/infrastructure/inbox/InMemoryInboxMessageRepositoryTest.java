package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InMemoryInboxMessageRepositoryTest {
    @Test
    void messageIsNotBeProcessedByDefault() {
        // Arrange
        var sut = new InMemoryInboxMessageRepository();
        var message = new InboxMessage(new TestIntegrationMessage());

        // Act
        var result = sut.isProcessed(message);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void messageIsProcessedAfterProcessing() {
        // Arrange
        var sut = new InMemoryInboxMessageRepository();
        var message = new InboxMessage(new TestIntegrationMessage());
        sut.markAsProcessed(message);

        // Act
        var result = sut.isProcessed(message);

        // Assert
        assertThat(result).isTrue();
    }

    private static class TestIntegrationMessage extends IntegrationMessage {}
}