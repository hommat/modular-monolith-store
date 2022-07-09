package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

public interface InboxMessageRepository {
    boolean isProcessed(InboxMessage message);
    void markAsProcessed(InboxMessage message);
}
