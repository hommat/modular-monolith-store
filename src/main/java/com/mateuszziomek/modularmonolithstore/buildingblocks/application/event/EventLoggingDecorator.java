package com.mateuszziomek.modularmonolithstore.buildingblocks.application.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class EventLoggingDecorator<T extends IntegrationEvent> implements IntegrationMessageHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingDecorator.class.getSimpleName());

    private final IntegrationMessageHandler<T> handler;

    public EventLoggingDecorator(final IntegrationMessageHandler<T> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null");

        this.handler = handler;
    }

    @Override
    public Mono<Void> handle(final T event) {
        Preconditions.checkNotNull(event, "Event can't be null");
        LOG.info("Executing event {}", event.getClass().getSimpleName());

        return handler
                .handle(event)
                .doOnSuccess(result -> LOG.info(
                    "Event {} processed successfully",
                    event.getClass().getSimpleName()
                ))
                .doOnError(throwable -> LOG.info(
                    "Event {} processing failed - {}",
                    event.getClass().getSimpleName(),
                    throwable.getClass().getSimpleName()
                ));
    }
}
