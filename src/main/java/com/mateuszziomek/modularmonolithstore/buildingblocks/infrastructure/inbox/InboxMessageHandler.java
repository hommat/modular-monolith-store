package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import reactor.core.publisher.Mono;

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
    public Mono<Void> handle(final T event) {
        var inboxMessage = new InboxMessage(event);

        return inboxMessageRepository
                .isProcessed(inboxMessage)
                .flatMap(isProcessed -> Boolean.TRUE.equals(isProcessed) ? Mono.empty() : Mono.just(isProcessed))
                .flatMap(result -> handler.handle(event)
                        .switchIfEmpty(inboxMessageRepository.markAsProcessed(inboxMessage))
                );
    }
}
