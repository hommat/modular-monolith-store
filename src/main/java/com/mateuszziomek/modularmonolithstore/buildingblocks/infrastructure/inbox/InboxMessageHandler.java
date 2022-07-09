package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import io.vavr.control.Try;

public class InboxMessageHandler<T extends IntegrationMessage> implements IntegrationMessageHandler<T> {
    private final InboxMessageRepository inboxMessageRepository;
    private final IntegrationMessageHandler<T> handler;

    public InboxMessageHandler(
            final InboxMessageRepository inboxMessageRepository,
            final IntegrationMessageHandler<T> handler
    ) {
        Preconditions.checkNotNull(inboxMessageRepository, "Inbox message repository can't be null");
        Preconditions.checkNotNull(handler, "Message handler repository can't be null");

        this.inboxMessageRepository = inboxMessageRepository;
        this.handler = handler;
    }

    @Override
    public Try<Void> handle(final T event) {
        var inboxMessage = new InboxMessage(event);
        if (inboxMessageRepository.isProcessed(inboxMessage)) {
            return Try.success(null);
        }

        var result = handler.handle(event);
        if (result.isSuccess()) {
            inboxMessageRepository.markAsProcessed(inboxMessage);
        }

        return result;
    }
}
