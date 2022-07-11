package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import reactor.core.publisher.Mono;

public interface InboxMessageRepository {
    Mono<Boolean> isProcessed(InboxMessage message);
    Mono<Void> markAsProcessed(InboxMessage message);
}
