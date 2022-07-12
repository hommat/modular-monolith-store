package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandLoggingDecorator;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidationDecorator;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartValidator;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;

public class CommandBusFactory {
    private CommandBusFactory() {}

    public static CommandBus create(final CartRepository cartRepository) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        final var commandBus = new CommandBus();

        commandBus.registerCommand(
                CreateCartCommand.class,
                new CommandLoggingDecorator<>(
                        new CommandValidationDecorator<>(
                                new CreateCartHandler(cartRepository),
                                new CreateCartValidator()
                        )
                )
        );

        return commandBus;
    }
}
