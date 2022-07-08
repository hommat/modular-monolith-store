package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import io.vavr.control.Try;

public interface MessageBus {
    <T extends IntegrationMessage> Try<Void> publish(T integrationEvent);
    <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler);
}
