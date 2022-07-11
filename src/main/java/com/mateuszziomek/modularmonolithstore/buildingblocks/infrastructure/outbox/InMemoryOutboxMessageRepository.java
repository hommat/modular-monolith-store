package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import io.vavr.collection.List;
import io.vavr.control.Option;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InMemoryOutboxMessageRepository implements OutboxMessageRepository {
    private final OutboxMessageNormalizer normalizer;

    public InMemoryOutboxMessageRepository(final OutboxMessageNormalizer normalizer) {
        Preconditions.checkNotNull(normalizer, "Normalizer can't be null");

        this.normalizer = normalizer;
    }

    private List<OutboxMessage> messages = List.empty();

    @Override
    public Flux<OutboxMessage> findUnprocessedMessages(int amount) {
        return Flux.fromIterable(messages.subSequence(0, Math.min(amount, messages.length())));
    }

    @Override
    public Mono<Void> saveDomainEvents(final List<? extends DomainEvent> messages) {
        var outboxMessages = messages
                .map(normalizer::normalize)
                .filter(option -> !option.isEmpty()).map(Option::get);

        synchronized (this) {
            this.messages = this.messages.appendAll(outboxMessages);
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> markAsProcessed(List<OutboxMessage> messages) {
        synchronized (this) {
            this.messages = this.messages.removeAll(messages);
        }

        return Mono.empty();
    }
}
