package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;

public class AggregateMethodMissingException extends RuntimeException {
    private final transient Class<DomainEvent> domainEventClass;

    public AggregateMethodMissingException(final Class<DomainEvent> domainEventClass) {
        Preconditions.checkNotNull(domainEventClass, "Domain event class can't be null");

        this.domainEventClass = domainEventClass;
    }

    public Class<DomainEvent> domainEventClass() {
        return domainEventClass;
    }
}
