package com.mateuszziomek.modularmonolithstore.buildingblocks.application.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationEvent;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationMessageHandler;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLoggingDecorator<T extends IntegrationEvent> implements IntegrationMessageHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(EventLoggingDecorator.class.getSimpleName());

    private final IntegrationMessageHandler<T> handler;

    public EventLoggingDecorator(final IntegrationMessageHandler<T> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null");

        this.handler = handler;
    }

    @Override
    public Try<Void> handle(final T event) {
        Preconditions.checkNotNull(event, "Event can't be null");
        LOG.info("Executing event {}", event.getClass().getSimpleName());

        var result = handler.handle(event);

        if (result.isSuccess()) {
            LOG.info("Event {} processed successfully", event.getClass().getSimpleName());
        } else {
            LOG.info(
                "Event {} processing failed ({})",
                event.getClass().getSimpleName(),
                result.getCause().getMessage()
            );
        }

        return result;
    }
}
