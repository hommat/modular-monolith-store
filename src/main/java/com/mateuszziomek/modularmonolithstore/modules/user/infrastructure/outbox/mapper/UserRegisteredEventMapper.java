package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox.mapper;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationMessageMapper;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.integration.event.UserRegisteredIntegrationEvent;

public class UserRegisteredEventMapper implements IntegrationMessageMapper<UserRegisteredDomainEvent, UserRegisteredIntegrationEvent> {
    @Override
    public UserRegisteredIntegrationEvent map(UserRegisteredDomainEvent domainEvent) {
        return new UserRegisteredIntegrationEvent(domainEvent.userId().value(), domainEvent.userId().toString());
    }

    @Override
    public boolean isSupported(DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        return domainEvent instanceof UserRegisteredDomainEvent;
    }
}
