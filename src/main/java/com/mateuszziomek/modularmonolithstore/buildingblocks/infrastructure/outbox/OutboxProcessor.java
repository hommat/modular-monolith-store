package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import io.vavr.collection.List;
import reactor.core.publisher.Mono;

public class OutboxProcessor {
    private final MessageBus messageBus;
    private final OutboxMessageRepository messageRepository;

    public OutboxProcessor(final MessageBus messageBus, final OutboxMessageRepository messageRepository) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");
        Preconditions.checkNotNull(messageRepository, "Message repository can't be null");

        this.messageBus = messageBus;
        this.messageRepository = messageRepository;
    }

    public Mono<Void> process(int amount) {
        messageRepository.findUnprocessedMessages(amount);

        return messageRepository
                .findUnprocessedMessages(amount)
                .concatMap(message -> messageBus
                        .publish(message.message())
                        .map(result -> message)
                        .defaultIfEmpty(message)
                )
                .onErrorStop()
                .collect(List.collector())
                .flatMap(messageRepository::markAsProcessed);
    }
}
