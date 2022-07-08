package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;

public class CommandBusFactory {
    private CommandBusFactory() {}

    public static CommandBus create(final CartRepository cartRepository) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        final var commandBus = new CommandBus();

        commandBus.registerCommand(
                CreateCartCommand.class,
                new CreateCartHandler(cartRepository)
        );

        return commandBus;
    }
}
