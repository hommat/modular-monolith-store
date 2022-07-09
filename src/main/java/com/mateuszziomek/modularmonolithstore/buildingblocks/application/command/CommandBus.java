package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;

import java.util.HashMap;

public class CommandBus {
    private final HashMap<Class<? extends Command>, CommandHandler<Command>> handlers = new HashMap<>();

    public <T extends Command> Try<Void> dispatch(final T command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var handler = handlers.get(command.getClass());
        if (handler == null) {
            return Try.failure(new CommandHandlerNotFoundException());
        }

        return handler.handle(command);
    }

    public <T extends Command> Try<Void> registerCommand(final Class<T> command, final CommandHandler<T> handler) {
        Preconditions.checkNotNull(command, "Command can't be null");
        Preconditions.checkNotNull(handler, "Handler can't be null");

        if (handlers.containsKey(command)) {
            return Try.failure(new CommandHandlerAlreadyRegisteredException());
        }

        handlers.put(command, (CommandHandler<Command>) handler);

        return Try.success(null);
    }
}
