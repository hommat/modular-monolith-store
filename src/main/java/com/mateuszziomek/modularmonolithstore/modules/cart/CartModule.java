package com.mateuszziomek.modularmonolithstore.modules.cart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandDispatcher;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleCommandDispatcher;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.event.UserRegisteredHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.command.CommandDispatcherFactory;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.domain.InMemoryCartRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.integration.event.UserRegisteredIntegrationEvent;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public class CartModule implements ModuleCommandDispatcher {
    private final CommandDispatcher commandDispatcher;

    private CartModule(final CommandDispatcher commandDispatcher) {
        Preconditions.checkNotNull(commandDispatcher, "Command dispatcher can't be null");

        this.commandDispatcher = commandDispatcher;
    }

    public static CartModule initialize(MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var commandDispatcher = CommandDispatcherFactory.create(new InMemoryCartRepository());

        messageBus.subscribe(UserRegisteredIntegrationEvent.class, new UserRegisteredHandler(commandDispatcher));

        return new CartModule(commandDispatcher);
    }

    @Override
    public Mono<Try<Void>> dispatchCommand(Command command) {
        return commandDispatcher.dispatch(command);
    }
}
