package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import io.vavr.collection.List;

public interface OutboxMessageRepository {
    List<OutboxMessage> findUnprocessedMessages(int amount);
    void saveDomainEvents(List<DomainEvent> messages);
    void markAsProcessed(List<OutboxMessage> messages);
}
