package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class CommandDispatcher {
    private final HashMap<Class<? extends Command>, ReactiveCommandHandler<Command>> handlers = new HashMap<>();

    public synchronized <T extends Command> Mono<Try<Void>> dispatch(final T command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var handler = handlers.get(command.getClass());
        if (handler == null) {
            return Mono.just(Try.failure(new RuntimeException("Command handler not found")));
        }

        return handler.handle(command);
    }

    public <T extends Command> Try<Void> registerCommand(final Class<T> command, final ReactiveCommandHandler<T> handler) {
        Preconditions.checkNotNull(command, "Command can't be null");
        Preconditions.checkNotNull(handler, "Handler can't be null");

        if (handlers.containsKey(command)) {
            return Try.failure(new RuntimeException("Command handler already registered"));
        }

        handlers.put(command, (ReactiveCommandHandler<Command>) handler);

        return Try.success(null);
    }
}
