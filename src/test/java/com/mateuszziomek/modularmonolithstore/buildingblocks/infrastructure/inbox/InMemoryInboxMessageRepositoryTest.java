package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class InMemoryInboxMessageRepositoryTest {
    @Test
    void messageIsNotProcessedByDefault() {
        // Arrange
        var sut = new InMemoryInboxMessageRepository();
        var message = new InboxMessage(new TestIntegrationMessage());

        // Act
        var result = sut.isProcessed(message);

        // Assert
        StepVerifier
                .create(result)
                .expectNext(false)
                .verifyComplete();
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
        StepVerifier
                .create(result)
                .expectNext(true)
                .verifyComplete();
    }

    private static class TestIntegrationMessage extends IntegrationMessage {}
}