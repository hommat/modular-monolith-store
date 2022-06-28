package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Try;

public abstract class Entity {
    private List<DomainEvent> domainEvents = List.empty();

    public void addDomainEvent(final DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        domainEvents = domainEvents.append(domainEvent);
    }

    public void clearDomainEvents() {
        domainEvents = List.empty();
    }

    public List<DomainEvent> domainEvents() {
        return domainEvents;
    }

    public Try<Void> checkRule(final BusinessRule businessRule) {
        Preconditions.checkNotNull(businessRule, "Business rule can't be null");

        return businessRule.isBroken() ? Try.failure(new BusinessRuleException(businessRule)) : Try.success(null);
    }
}
