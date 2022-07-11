package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.Preconditions;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class InMemoryMessageBusTest {
    @Test
    void publishingMessageIsSuccessWhenNumberOfSubscribersIsZero() {
        // Arrange
        var sut = new InMemoryMessageBus();
        var message = new TestIntegrationMessage();

        // Act
        var result = sut.publish(message);

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();
    }

    @Test
    void publishingMessageIsSuccessWhenEverySubscriberResponseIsSuccess() {
        // Arrange
        var sut = new InMemoryMessageBus();
        var message = new TestIntegrationMessage();
        var handler1 = new SuccessTestIntegrationMessageHandler();
        var handler2 = new SuccessTestIntegrationMessageHandler();
        var handler3 = new SuccessTestIntegrationMessageHandler();

        sut.subscribe(TestIntegrationMessage.class, handler1);
        sut.subscribe(TestIntegrationMessage.class, handler2);
        sut.subscribe(TestIntegrationMessage.class, handler3);

        // Act
        var result = sut.publish(message);

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();
    }

    @Test
    void publishingMessageIsFailureWhenSomeSubscriberResponseIsFailure() {
        // Arrange
        var sut = new InMemoryMessageBus();
        var message = new TestIntegrationMessage();
        var handler1 = new SuccessTestIntegrationMessageHandler();
        var handler2 = new FailureTestIntegrationMessageHandler();
        var handler3 = new SuccessTestIntegrationMessageHandler();

        sut.subscribe(TestIntegrationMessage.class, handler1);
        sut.subscribe(TestIntegrationMessage.class, handler2);
        sut.subscribe(TestIntegrationMessage.class, handler3);

        // Act
        var result = sut.publish(message);

        // Assert
        StepVerifier
                .create(result)
                .verifyError();
    }

    @Test
    void publishingMessageCallsOnlyHandlersOfGivenMessageType() {
        // Arrange
        var sut = new InMemoryMessageBus();
        var message = new AnotherIntegrationMessage();
        var handler = new FailureTestIntegrationMessageHandler();

        sut.subscribe(TestIntegrationMessage.class, handler);

        // Act
        var result = sut.publish(message);

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();
    }

    static class TestIntegrationMessage extends IntegrationMessage {}

    static class AnotherIntegrationMessage extends IntegrationMessage {}

    static class SuccessTestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        @Override
        public Mono<Void> handle(final TestIntegrationMessage message) {
            Preconditions.notNull(message, "Message can't be null");

            return Mono.empty();
        }
    }

    static class FailureTestIntegrationMessageHandler implements IntegrationMessageHandler<TestIntegrationMessage> {
        @Override
        public Mono<Void> handle(final TestIntegrationMessage message) {
            Preconditions.notNull(message, "Message can't be null");

            return Mono.error(new RuntimeException());
        }
    }
}