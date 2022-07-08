package com.mateuszziomek.modularmonolithstore.modules.cart.application.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.IntegrationEventHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.integration.event.UserRegisteredIntegrationEvent;
import io.vavr.control.Try;

public class UserRegisteredHandler implements IntegrationEventHandler<UserRegisteredIntegrationEvent> {
    private final CommandBus commandBus;

    public UserRegisteredHandler(final CommandBus commandBus) {
        Preconditions.checkNotNull(commandBus, "Command dispatcher can't be null");

        this.commandBus = commandBus;
    }

    @Override
    public Try<Void> handle(final UserRegisteredIntegrationEvent event) {
        Preconditions.checkNotNull(event, "Event can't be null");

        var command = new CreateCartCommand(event.userId());

        return commandBus.dispatch(command);
    }
}
