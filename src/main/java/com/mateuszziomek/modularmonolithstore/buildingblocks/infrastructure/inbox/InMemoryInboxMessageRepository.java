package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public class InMemoryInboxMessageRepository implements InboxMessageRepository {
    private Set<InboxMessage> processedMessages = new HashSet<>();

    @Override
    public synchronized Mono<Boolean> isProcessed(InboxMessage message) {
        return Mono.just(processedMessages.contains(message));
    }

    @Override
    public synchronized Mono<Void> markAsProcessed(InboxMessage message) {
        processedMessages.add(message);

        return Mono.empty();
    }
}
