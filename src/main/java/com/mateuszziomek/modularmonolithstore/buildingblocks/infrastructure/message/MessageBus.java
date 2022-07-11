package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import reactor.core.publisher.Mono;

public interface MessageBus {
    <T extends IntegrationMessage> Mono<Void> publish(T integrationEvent);
    <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler);
}
