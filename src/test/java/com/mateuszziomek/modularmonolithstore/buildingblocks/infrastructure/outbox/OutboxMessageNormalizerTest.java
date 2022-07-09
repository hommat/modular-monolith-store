package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OutboxMessageNormalizerTest {
    @Test
    void messagesWithSpecifiedMapperAreNormalized() {
        // Arrange
        var mapper = new TestDomainEventMapper();
        var sut = new OutboxMessageNormalizer(List.of(mapper));

        // Act
        var result = sut.normalize(new TestDomainEvent());

        // Assert
        assertThat(result.isDefined()).isTrue();
        assertThat(result.get().message()).isInstanceOf(TestIntegrationMessage.class);
    }

    @Test
    void messagesWithoutSpecifiedMapperAreEmpty() {
        // Arrange
        var mapper = new TestDomainEventMapper();
        var sut = new OutboxMessageNormalizer(List.of(mapper));

        // Act
        var result = sut.normalize(new AnotherTestDomainEvent());

        // Assert
        assertThat(result.isEmpty()).isTrue();
    }

    private static class TestDomainEvent extends DomainEvent {}

    private static class AnotherTestDomainEvent extends DomainEvent {}

    private static class TestIntegrationMessage extends IntegrationMessage {}

    private static class TestDomainEventMapper implements IntegrationMessageMapper<TestDomainEvent, TestIntegrationMessage> {
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