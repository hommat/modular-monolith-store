package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(result.length()).isEqualTo(3);
    }

    @Test
    void processedMessagesShouldNotBeRetrieved() {
        // Arrange
        var normalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var repository = new InMemoryOutboxMessageRepository(normalizer);
        repository.saveDomainEvents(List.of(new DomainEvent(), new DomainEvent(), new DomainEvent()));
        var unprocessedMessages = repository.findUnprocessedMessages(5);
        repository.markAsProcessed(unprocessedMessages);

        // Act
        var result = repository.findUnprocessedMessages(5);

        // Assert
        assertThat(result.length()).isZero();
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