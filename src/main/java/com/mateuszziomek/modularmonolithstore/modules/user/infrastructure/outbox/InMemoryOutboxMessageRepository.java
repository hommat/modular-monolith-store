package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageNormalizer;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.message.mapper.UserRegisteredEventMapper;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class InMemoryOutboxMessageRepository implements OutboxMessageRepository {
    private final OutboxMessageNormalizer normalizer = new OutboxMessageNormalizer(
            List.of(new UserRegisteredEventMapper())
    );

    private List<OutboxMessage> messages = List.empty();

    @Override
    public List<OutboxMessage> findUnprocessedMessages(int amount) {
        return messages.subSequence(0, Math.min(amount, messages.length()));
    }

    @Override
    public void saveDomainEvents(final List<DomainEvent> messages) {
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
