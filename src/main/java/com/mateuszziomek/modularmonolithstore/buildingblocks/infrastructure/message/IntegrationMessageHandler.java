package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import reactor.core.publisher.Mono;

public interface IntegrationMessageHandler<T extends IntegrationMessage> {
    Mono<Void> handle(T message);
}
