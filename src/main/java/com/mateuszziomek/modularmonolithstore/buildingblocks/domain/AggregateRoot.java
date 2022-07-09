package com.mateuszziomek.modularmonolithstore.buildingblocks.domain;

import com.google.common.base.Preconditions;
import io.vavr.collection.List;

public abstract class AggregateRoot extends Entity {
    private List<DomainEvent> pendingDomainEvents = List.empty();

    public void raiseEvent(final DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        applyChange(domainEvent, true);
    }

    public void markDomainEventsAsCommitted() {
        pendingDomainEvents = List.empty();
    }

    public List<DomainEvent> pendingDomainEvents() {
        return pendingDomainEvents;
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
