package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.InMemoryMessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OutboxProcessorTest {
    @Test
    void marksMessagesAsProcessedIfPublishResultIsSuccess() {
        // Arrange
        var messageBus = new InMemoryMessageBus();
        var outboxNormalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var outboxRepository = new InMemoryOutboxMessageRepository(outboxNormalizer);
        var handler = new SuccessTestIntegrationMessageHandler();
        var sut = new OutboxProcessor(messageBus, outboxRepository);

        outboxRepository.saveDomainEvents(List.of(new TestDomainEvent(), new TestDomainEvent()));
        messageBus.subscribe(TestIntegrationMessage.class, handler);

        // Act
        sut.process(2);

        // Assert
        assertThat(outboxRepository.findUnprocessedMessages(2).length()).isZero();
        assertThat(handler.processAmount).isEqualTo(2);
    }

    @Test
    void doesNotMarkMessagesAsProcessedIfPublishResultIsFailure() {
        // Arrange
        var messageBus = new InMemoryMessageBus();
        var outboxNormalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var outboxRepository = new InMemoryOutboxMessageRepository(outboxNormalizer);
        var handler = new FailureTestIntegrationMessageHandler();
        var sut = new OutboxProcessor(messageBus, outboxRepository);

        outboxRepository.saveDomainEvents(List.of(new TestDomainEvent(), new TestDomainEvent()));
        messageBus.subscribe(TestIntegrationMessage.class, handler);

        // Act
        sut.process(2);

        // Assert
        assertThat(outboxRepository.findUnprocessedMessages(2).length()).isEqualTo(2);
        assertThat(handler.processAmount).isEqualTo(1);
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

    private static class SuccessTestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        public int processAmount = 0;

        @Override
        public Try<Void> handle(TestIntegrationMessage message) {
            processAmount += 1;

            return Try.success(null);
        }
    }

    private static class FailureTestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        public int processAmount = 0;

        @Override
        public Try<Void> handle(TestIntegrationMessage message) {
            processAmount += 1;

            return Try.failure(new RuntimeException());
        }
    }
}