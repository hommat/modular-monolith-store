package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class InMemoryOutboxMessageRepository implements OutboxMessageRepository {
    private final OutboxMessageNormalizer normalizer;

    public InMemoryOutboxMessageRepository(final OutboxMessageNormalizer normalizer) {
        Preconditions.checkNotNull(normalizer, "Normalizer can't be null");

        this.normalizer = normalizer;
    }

    private List<OutboxMessage> messages = List.empty();

    @Override
    public List<OutboxMessage> findUnprocessedMessages(int amount) {
        return messages.subSequence(0, Math.min(amount, messages.length()));
    }

    @Override
    public void saveDomainEvents(final List<? extends DomainEvent> messages) {
        var outboxMessages = messages
                .map(normalizer::normalize)
                .filter(option -> !option.isEmpty()).map(Option::get);

        this.messages = this.messages.appendAll(outboxMessages);
    }

    @Override
    public void markAsProcessed(List<OutboxMessage> messages) {
        this.messages = this.messages.removeAll(messages);
    }
}
