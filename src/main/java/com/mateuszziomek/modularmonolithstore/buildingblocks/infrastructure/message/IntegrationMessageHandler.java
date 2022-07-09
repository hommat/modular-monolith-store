package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import io.vavr.control.Try;

public interface IntegrationMessageHandler<T extends IntegrationMessage> {
    Try<Void> handle(T message);
}
