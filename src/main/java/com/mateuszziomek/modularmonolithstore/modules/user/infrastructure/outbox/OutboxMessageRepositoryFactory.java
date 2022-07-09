package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.InMemoryOutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageNormalizer;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.message.mapper.UserRegisteredEventMapper;
import io.vavr.collection.List;

public class OutboxMessageRepositoryFactory {
    private OutboxMessageRepositoryFactory() {}

    public static OutboxMessageRepository create() {
        var mappers = List.of(new UserRegisteredEventMapper());
        var outboxNormalizer = new OutboxMessageNormalizer(mappers);
        
        return new InMemoryOutboxMessageRepository(outboxNormalizer);
    }
}
