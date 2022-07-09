package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.message.mapper;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.event.UserRegisteredDomainEvent;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;

public class UserRegisteredEventMapper implements IntegrationMessageMapper<UserRegisteredDomainEvent, UserRegisteredIntegrationEvent> {
    @Override
    public UserRegisteredIntegrationEvent map(UserRegisteredDomainEvent domainEvent) {
        return new UserRegisteredIntegrationEvent(domainEvent.userId().value(), domainEvent.username().value());
    }

    @Override
    public boolean isSupported(DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        return domainEvent instanceof UserRegisteredDomainEvent;
    }
}
