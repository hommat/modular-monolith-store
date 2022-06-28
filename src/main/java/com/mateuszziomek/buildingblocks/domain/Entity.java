package com.mateuszziomek.buildingblocks.domain;

import com.google.common.base.Preconditions;
import io.vavr.collection.LinkedHashSet;
import io.vavr.collection.Set;
import io.vavr.control.Try;

public abstract class Entity {
    private Set<DomainEvent> domainEvents = LinkedHashSet.empty();

    public void addDomainEvent(final DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        domainEvents = domainEvents.add(domainEvent);
    }

    public void clearDomainEvents() {
        domainEvents = LinkedHashSet.empty();
    }

    public Set<DomainEvent> domainEvents() {
        return domainEvents;
    }

    public Try<Void> checkRule(final BusinessRule businessRule) {
        Preconditions.checkNotNull(businessRule, "Business rule can't be null");

        return businessRule.isBroken() ? Try.failure(new BusinessRuleException(businessRule)) : Try.success(null);
    }
}
