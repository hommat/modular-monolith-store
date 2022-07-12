package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import com.google.common.base.Preconditions;
import reactor.core.publisher.Mono;

public class CommandValidationDecorator<T extends Command> implements CommandHandler<T> {
    private final CommandHandler<T> handler;
    private final CommandValidator<T> validator;

    public CommandValidationDecorator(final CommandHandler<T> handler, final CommandValidator<T> validator) {
        Preconditions.checkNotNull(handler, "Handler can't be null");
        Preconditions.checkNotNull(validator, "Handler can't be null");

        this.handler = handler;
        this.validator = validator;
    }

    @Override
    public Mono<Void> handle(final T command) {
        final var result = validator.validate(command);

        if (result.isInvalid()) {
            return Mono.error(new CommandValidationException(result));
        }

        return handler.handle(command);
    }
}
