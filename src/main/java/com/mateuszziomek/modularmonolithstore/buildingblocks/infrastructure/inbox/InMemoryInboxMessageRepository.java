package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class InMemoryInboxMessageRepository implements InboxMessageRepository {
    private Set<InboxMessage> processedMessages = HashSet.empty();

    @Override
    public boolean isProcessed(InboxMessage message) {
        return processedMessages.contains(message);
    }

    @Override
    public void markAsProcessed(InboxMessage message) {
        processedMessages = processedMessages.add(message);
    }
}
