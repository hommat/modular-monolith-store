package com.mateuszziomek.modularmonolithstore.buildingblocks.application.command;

import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLoggingDecorator<T extends Command> implements CommandHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLoggingDecorator.class.getSimpleName());
    private final CommandHandler<T> handler;

    public CommandLoggingDecorator(final CommandHandler<T> handler) {
        Preconditions.checkNotNull(handler, "Handler can't be null");

        this.handler = handler;
    }

    @Override
    public Try<Void> handle(final T command) {
        LOG.info("Executing command {}", command.getClass().getSimpleName());

        var result = handler.handle(command);

        if (result.isSuccess()) {
            LOG.info("Command {} processed successfully", command.getClass().getSimpleName());
        } else {
            LOG.info(
                "Command {} processing failed ({})",
                command.getClass().getSimpleName(),
                result.getCause().getMessage()
            );
        }

        return result;
    }
}
