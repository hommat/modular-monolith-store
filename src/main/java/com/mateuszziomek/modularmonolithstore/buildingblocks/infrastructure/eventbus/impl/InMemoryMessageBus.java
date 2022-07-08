package com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.impl;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationMessage;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationMessageHandler;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.MessageBus;
import io.vavr.collection.List;
import io.vavr.control.Try;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.function.Function;

public class InMemoryMessageBus implements MessageBus {
    private final HashMap<Class<IntegrationMessage>, List<IntegrationMessageHandler>> handlers = new HashMap<>();

    @Override
    public <T extends IntegrationMessage> Mono<Try<Void>> publish(T integrationMessage) {
        var eventHandlers = handlers.get(integrationMessage.getClass());

        if (eventHandlers == null || eventHandlers.isEmpty()) {
            return Mono.just(Try.success(null));
        }

        List<Mono<Try<Void>>> handlerMonos = eventHandlers.map(handler -> handler.handle(integrationMessage));

        return Flux
                .fromIterable(handlerMonos)
                .flatMap(Function.identity())
                .reduce((reductionTry, currentTry) -> reductionTry.isSuccess() ? currentTry : reductionTry);
    }

    @Override
    public <T extends IntegrationMessage> void subscribe(Class<T> event, IntegrationMessageHandler<T> handler) {
        if (handlers.containsKey(event)) {
            handlers.put((Class<IntegrationMessage>) event, handlers.get(event).append(handler));
        } else {
            handlers.put((Class<IntegrationMessage>) event, List.of(handler));
        }
    }
}
