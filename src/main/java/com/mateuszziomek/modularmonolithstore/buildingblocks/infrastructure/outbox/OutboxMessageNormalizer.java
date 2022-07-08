package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationMessageMapper;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationMessageMapperMissingException;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class OutboxMessageNormalizer {
    private final List<? extends IntegrationMessageMapper> mappers;

    public OutboxMessageNormalizer(final List<? extends IntegrationMessageMapper> mappers) {
        Preconditions.checkNotNull(mappers, "Mappers can't be null");

        this.mappers = mappers;
    }

    public Try<List<OutboxMessage>> normalizeMany(final List<DomainEvent> domainEvents) {
        Preconditions.checkNotNull(domainEvents, "Domain events can't be null");

        List<OutboxMessage> outboxMessages = List.empty();

        for (DomainEvent domainEvent : domainEvents) {
            var mapper = mappers.find(m -> m.isSupported(domainEvent));

            if (mapper.isEmpty()) {
                return Try.failure(new IntegrationMessageMapperMissingException());
            }

            var integrationMessage = mapper.get().map(domainEvent);
            var outboxMessage = new OutboxMessage(integrationMessage);

            outboxMessages = outboxMessages.append(outboxMessage);
        }

        return Try.success(outboxMessages);
    }
}
