package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class OutboxMessageNormalizer {
    private final List<? extends IntegrationMessageMapper> mappers;

    public OutboxMessageNormalizer(final List<? extends IntegrationMessageMapper> mappers) {
        Preconditions.checkNotNull(mappers, "Mappers can't be null");

        this.mappers = mappers;
    }

    public Option<OutboxMessage> normalize(final DomainEvent domainEvent) {
        Preconditions.checkNotNull(domainEvent, "Domain event can't be null");

        var mapper = mappers.find(m -> m.isSupported(domainEvent));
        if (mapper.isEmpty()) {
            return Option.none();
        }

        var integrationMessage = mapper.get().map(domainEvent);
        var outboxMessage = new OutboxMessage(integrationMessage);

        return Option.of(outboxMessage);
    }
}
