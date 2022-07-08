package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;

public interface IntegrationMessageMapper<T extends DomainEvent, U extends IntegrationMessage> {
    U map(T domainEvent);
    boolean isSupported(DomainEvent domainEvent);
}
