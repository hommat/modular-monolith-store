package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OutboxMessageRepository {
    Flux<OutboxMessage> findUnprocessedMessages(int amount);
    Mono<Void> saveDomainEvents(List<DomainEvent> messages);
    Mono<Void> markAsProcessed(List<OutboxMessage> messages);
}
