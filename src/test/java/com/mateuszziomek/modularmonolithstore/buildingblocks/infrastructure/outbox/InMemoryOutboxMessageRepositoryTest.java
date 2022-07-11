package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class InMemoryOutboxMessageRepositoryTest {
    @Test
    void savedDomainEventsCanBeRetrieved() {
        // Arrange
        var normalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var repository = new InMemoryOutboxMessageRepository(normalizer);
        repository.saveDomainEvents(List.of(new TestDomainEvent(), new TestDomainEvent(), new TestDomainEvent()));

        // Act
        var result = repository.findUnprocessedMessages(5);

        // Assert
        StepVerifier
                .create(result)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void processedMessagesShouldNotBeRetrieved() {
        // Arrange
        var normalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var repository = new InMemoryOutboxMessageRepository(normalizer);
        repository.saveDomainEvents(List.of(new DomainEvent(), new DomainEvent(), new DomainEvent())).block();
        var unprocessedMessages = repository
                .findUnprocessedMessages(5)
                .collect(List.collector()).block();
        repository.markAsProcessed(unprocessedMessages).block();

        // Act
        var result = repository.findUnprocessedMessages(5);

        // Assert
        StepVerifier
                .create(result)
                .expectComplete();
    }

    private static class TestDomainEvent extends DomainEvent {}

    private static class TestIntegrationMessage extends IntegrationMessage {}

    private static class TestIntegrationMessageMapper implements IntegrationMessageMapper<TestDomainEvent, TestIntegrationMessage> {

        @Override
        public TestIntegrationMessage map(TestDomainEvent domainEvent) {
            return new TestIntegrationMessage();
        }

        @Override
        public boolean isSupported(DomainEvent domainEvent) {
            return domainEvent instanceof TestDomainEvent;
        }
    }
}