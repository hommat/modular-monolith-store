package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.command;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandDispatcher;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;

public class CommandDispatcherFactory {
    private CommandDispatcherFactory() {}

    public static CommandDispatcher create(final CartRepository cartRepository) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        final var commandDispatcher = new CommandDispatcher();

        commandDispatcher.registerCommand(
                CreateCartCommand.class,
                new CreateCartHandler(cartRepository)
        );

        return commandDispatcher;
    }
}
