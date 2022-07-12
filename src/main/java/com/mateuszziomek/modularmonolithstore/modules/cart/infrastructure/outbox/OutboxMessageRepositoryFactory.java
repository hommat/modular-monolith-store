package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageMapper;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.InMemoryOutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageNormalizer;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import io.vavr.collection.List;

public class OutboxMessageRepositoryFactory {
    private OutboxMessageRepositoryFactory() {}

    public static OutboxMessageRepository create() {
        List<IntegrationMessageMapper> mappers = List.empty();
        var outboxNormalizer = new OutboxMessageNormalizer(mappers);

        return new InMemoryOutboxMessageRepository(outboxNormalizer);
    }
}
