package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import reactor.core.publisher.Mono;

public class TestMessageBus extends InMemoryMessageBus {
    public List<IntegrationMessage> publishedMessages = List.empty();

    @Override
    public <T extends IntegrationMessage> Mono<Void> publish(final T integrationEvent) {
        publishedMessages = publishedMessages.append(integrationEvent);

        return super.publish(integrationEvent);
    }

    @Override
    public <T extends IntegrationMessage> void subscribe(
            final Class<T> event,
            final IntegrationMessageHandler<T> handler
    ) {
        Preconditions.checkNotNull(event, "Event can't be null");
        Preconditions.checkNotNull(handler, "Handler can't be null");

        super.subscribe(event, handler);
    }

    public void clearPublishedMessages() {
        publishedMessages = List.empty();
    }
}
