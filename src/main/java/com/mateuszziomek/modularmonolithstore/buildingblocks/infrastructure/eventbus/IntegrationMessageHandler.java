package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus;

import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public interface IntegrationMessageHandler<T extends IntegrationMessage> {
    Mono<Try<Void>> handle(T event);
}
