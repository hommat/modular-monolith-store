package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AggregateRootTest {
    @Test
    void domainActionsCreatesPendingEvents() {
        // Arrange
        var sut = new TestAggregateRoot();

        // Act
        sut.domainAction();

        // Assert
        assertThat(sut.pendingDomainEvents().length()).isEqualTo(1);
    }

    @Test
    void domainActionsLoadsDomainEventsIntoAggregate() {
        // Arrange
        var sut = new TestAggregateRoot();

        // Act
        sut.domainAction();

        // Assert
        assertThat(sut.testDomainEventsLoaded).isEqualTo(1);
    }

    @Test
    void domainEventsCanBeMarkedAsCommitted() {
        // Arrange
        var sut = new TestAggregateRoot();
        sut.domainAction();

        // Act
        sut.markDomainEventsAsCommitted();

        // Assert
        assertThat(sut.pendingDomainEvents().length()).isZero();
    }

    private static class TestDomainEvent extends DomainEvent {}

    private static class TestAggregateRoot extends AggregateRoot {
        public int testDomainEventsLoaded = 0;

        public void domainAction() {
            raiseEvent(new TestDomainEvent());
        }

        public void on(TestDomainEvent event) {
            testDomainEventsLoaded += 1;
        }

        @Override
        public AggregateId id() {
            return null;
        }
    }
}
