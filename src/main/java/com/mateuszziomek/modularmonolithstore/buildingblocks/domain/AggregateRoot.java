package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Try;

public abstract class AggregateRoot {
    private List<DomainEvent> committedDomainEvents = List.empty();
    private List<DomainEvent> pendingDomainEvents = List.empty();

    public void raiseEvent(final DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        applyChange(domainEvent, true);
    }

    public void markDomainEventsAsCommitted() {
        committedDomainEvents = committedDomainEvents.appendAll(pendingDomainEvents);
        pendingDomainEvents = List.empty();
    }

    public List<DomainEvent> committedDomainEvents() {
        return committedDomainEvents;
    }

    public List<DomainEvent> pendingDomainEvents() {
        return pendingDomainEvents;
    }

    public Try<Void> checkRule(final BusinessRule businessRule) {
        Preconditions.checkNotNull(businessRule, "Business rule can't be null");

        return businessRule.isBroken() ? Try.failure(new BusinessRuleException(businessRule)) : Try.success(null);
    }

    private void applyChange(DomainEvent event, boolean isNew) {
        try {
            var method = getClass().getDeclaredMethod("on", event.getClass());
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            throw new AggregateMethodMissingException((Class<DomainEvent>) event.getClass());
        } catch (Exception e) {
            throw new RuntimeException("Error invoking event to aggregate", e);
        } finally {
            if (isNew) {
                pendingDomainEvents = pendingDomainEvents.append(event);
            }
        }
    }
}
