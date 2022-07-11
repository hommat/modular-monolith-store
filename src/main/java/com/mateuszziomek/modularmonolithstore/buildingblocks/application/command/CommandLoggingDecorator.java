package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class CommandLoggingDecorator<T extends Command> implements CommandHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLoggingDecorator.class.getSimpleName());
    private final CommandHandler<T> handler;

    public CommandLoggingDecorator(final CommandHandler<T> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null");

        this.handler = handler;
    }

    @Override
    public Mono<Void> handle(final T command) {
        LOG.info("Executing command {}", command.getClass().getSimpleName());

        return handler
                .handle(command)
                .doOnSuccess(result -> LOG.info(
                    "Command {} processed successfully",
                    command.getClass().getSimpleName()
                ))
                .doOnError(throwable -> LOG.info(
                        "Command {} processing failed - {}",
                        command.getClass().getSimpleName(),
                        throwable.getClass().getSimpleName()
                ));
    }
}
