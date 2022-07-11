package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import reactor.core.publisher.Mono;

public class InMemoryInboxMessageRepository implements InboxMessageRepository {
    private Set<InboxMessage> processedMessages = HashSet.empty();

    @Override
    public Mono<Boolean> isProcessed(InboxMessage message) {
        return Mono.just(processedMessages.contains(message));
    }

    @Override
    public Mono<Void> markAsProcessed(InboxMessage message) {
        synchronized (this) {
            processedMessages = processedMessages.add(message);
        }

        return Mono.empty();
    }
}
