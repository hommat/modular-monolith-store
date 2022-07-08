package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageNormalizer;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox.mapper.UserRegisteredEventMapper;
import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InMemoryOutboxMessageRepository implements OutboxMessageRepository {
    private final OutboxMessageNormalizer normalizer = new OutboxMessageNormalizer(
            List.of(new UserRegisteredEventMapper())
    );

    private List<OutboxMessage> messages = List.empty();

    @Override
    public Flux<OutboxMessage> findUnprocessedMessages(int amount) {
        return Flux.fromIterable(messages.subSequence(0, Math.min(amount, messages.length())));
    }

    @Override
    public Mono<Void> saveDomainEvents(final List<DomainEvent> messages) {
        // @TODO Try not to throw
        this.messages = normalizer
                .normalizeMany(messages)
                .getOrElseThrow(throwable -> new RuntimeException(throwable));

        return Mono.empty();
    }

    @Override
    public Mono<Void> markAsProcessed(List<OutboxMessage> messages) {
        this.messages = this.messages.removeAll(messages);

        return Mono.empty();
    }
}
