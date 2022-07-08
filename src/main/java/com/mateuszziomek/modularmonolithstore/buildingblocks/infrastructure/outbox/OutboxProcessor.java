package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.MessageBus;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class OutboxProcessor {
    private final MessageBus messageBus;
    private final OutboxMessageRepository messageRepository;

    public OutboxProcessor(final MessageBus messageBus, final OutboxMessageRepository messageRepository) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");
        Preconditions.checkNotNull(messageRepository, "Message repository can't be null");

        this.messageBus = messageBus;
        this.messageRepository = messageRepository;
    }

    public void process(int amount) {
        // @TODO remove reactor
        java.util.stream.Stream javaMessageStream = messageRepository
                .findUnprocessedMessages(amount)
                .toStream();

        List<OutboxMessage> messages = Stream.ofAll(javaMessageStream).toList();

        for (OutboxMessage message : messages) {
            var publishResult = messageBus.publish(message.message()).block();
            if (publishResult == null || publishResult.isFailure()) {
                return;
            }
        }

        messageRepository.markAsProcessed(messages);
    }
}
