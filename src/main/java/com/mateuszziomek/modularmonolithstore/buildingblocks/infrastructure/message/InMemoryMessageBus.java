package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.HashMap;

public class InMemoryMessageBus implements MessageBus {
    private final HashMap<Class<? extends IntegrationMessage>, List<IntegrationMessageHandler>> handlers = new HashMap<>();

    @Override
    public <T extends IntegrationMessage> Try<Void> publish(T integrationMessage) {
        var eventHandlers = handlers.get(integrationMessage.getClass());

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            return Try.success(null);
        }

        return eventHandlers.map(handler -> handler
                .handle(integrationMessage))
                .reduce((reductionTry, currentTry) -> reductionTry.isSuccess() ? currentTry : reductionTry);
    }

    @Override
    public <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler) {
        if (handlers.containsKey(event)) {
            handlers.put(event, handlers.get(event).append(handler));
        } else {
            handlers.put(event, List.of(handler));
        }
    }
}
