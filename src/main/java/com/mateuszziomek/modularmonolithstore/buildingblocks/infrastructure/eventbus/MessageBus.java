package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus;

import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public interface MessageBus {
    <T extends IntegrationMessage> Mono<Try<Void>> publish(T integrationEvent);
    <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler);
}
