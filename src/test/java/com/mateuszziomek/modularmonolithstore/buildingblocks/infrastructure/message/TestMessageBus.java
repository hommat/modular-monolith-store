package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class TestMessageBus implements MessageBus {
    public List<IntegrationMessage> publishedMessages = List.empty();

    @Override
    public <T extends IntegrationMessage> Try<Void> publish(T integrationEvent) {
        Preconditions.checkNotNull(integrationEvent, "Event can't be null");

        publishedMessages = publishedMessages.append(integrationEvent);
        return Try.success(null);
    }

    @Override
    public <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler) {
        Preconditions.checkNotNull(event, "Event can't be null");
        Preconditions.checkNotNull(handler, "Handler can't be null");
    }

    public void clearPublishedMessages() {
        publishedMessages = List.empty();
    }
}
