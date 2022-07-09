package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import static org.assertj.core.api.Assertions.*;

public abstract class AbstractAggregateRootTest {
    protected void assertThatNumberOfPendingEventsIs(AggregateRoot aggregateRoot, int length) {
        assertThat(aggregateRoot.pendingDomainEvents().length()).isEqualTo(length);
    }
}
