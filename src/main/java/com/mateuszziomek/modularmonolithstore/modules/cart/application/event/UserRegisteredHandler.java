package com.mateuszziomek.modularmonolithstore.modules.cart.application.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandDispatcher;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.IntegrationEventHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.integration.event.UserRegisteredIntegrationEvent;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public class UserRegisteredHandler implements IntegrationEventHandler<UserRegisteredIntegrationEvent> {
    private final CommandDispatcher commandDispatcher;

    public UserRegisteredHandler(final CommandDispatcher commandDispatcher) {
        Preconditions.checkNotNull(commandDispatcher, "Command dispatcher can't be null");

        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public Mono<Try<Void>> handle(UserRegisteredIntegrationEvent event) {
        var command = new CreateCartCommand(event.userId());

        return commandDispatcher.dispatch(command);
    }
}
