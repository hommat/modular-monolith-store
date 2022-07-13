package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class InMemoryMessageBus implements MessageBus {
    private final HashMap<Class<? extends IntegrationMessage>, List<IntegrationMessageHandler>> handlers = new HashMap<>();

    @Override
    public synchronized  <T extends IntegrationMessage> Mono<Void> publish(T integrationMessage) {
        var eventHandlers = handlers.get(integrationMessage.getClass());

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            return Mono.empty();
        }

        List<Mono<Void>> monos = eventHandlers.map(handler -> handler.handle(integrationMessage));

        return Flux.concat(monos)
                .collectList()
                .then();
    }

    @Override
    public synchronized  <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler) {
        if (handlers.containsKey(event)) {
            handlers.put(event, handlers.get(event).append(handler));
        } else {
            handlers.put(event, List.of(handler));
        }
    }
}
