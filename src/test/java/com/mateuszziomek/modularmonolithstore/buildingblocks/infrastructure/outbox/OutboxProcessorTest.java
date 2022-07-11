package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.InMemoryMessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

        outboxRepository.saveDomainEvents(List.of(new TestDomainEvent(), new TestDomainEvent())).block();
        messageBus.subscribe(TestIntegrationMessage.class, handler);

        // Act
        sut.process(2).block();

        // Assert
        StepVerifier
                .create(outboxRepository.findUnprocessedMessages(2))
                .verifyComplete();
        assertThat(handler.processAmount).isEqualTo(2);
    }

    @Test
    void doesNotMarkMessagesAsProcessedIfPublishResultIsFailure() {
        // Arrange
        var messageBus = new InMemoryMessageBus();
        var outboxNormalizer = new OutboxMessageNormalizer(List.of(new TestIntegrationMessageMapper()));
        var outboxRepository = new InMemoryOutboxMessageRepository(outboxNormalizer);
        var successHandler = new SuccessTestIntegrationMessageHandler();
        var failureHandler = new FailureTestIntegrationMessageHandler();
        var sut = new OutboxProcessor(messageBus, outboxRepository);

        outboxRepository.saveDomainEvents(List.of(new TestDomainEvent(), new TestDomainEvent())).block();
        messageBus.subscribe(TestIntegrationMessage.class, successHandler);
        messageBus.subscribe(TestIntegrationMessage.class, failureHandler);

        // Act
        var result = sut.process(2);

        // Assert
        StepVerifier
                .create(result)
                .verifyError();

        StepVerifier
                .create(outboxRepository.findUnprocessedMessages(2))
                .expectNextCount(2)
                .verifyComplete();

        assertThat(successHandler.processAmount).isEqualTo(1);
        assertThat(failureHandler.processAmount).isEqualTo(1);
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
        public Mono<Void> handle(TestIntegrationMessage message) {
            processAmount += 1;

            return Mono.empty();
        }
    }

    private static class FailureTestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        public int processAmount = 0;

        @Override
        public Mono<Void> handle(TestIntegrationMessage message) {
            processAmount += 1;

            return Mono.error(new RuntimeException("FailureTestIntegrationMessageHandler Exception"));
        }
    }
}