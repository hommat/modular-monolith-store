package com.mateuszziomek.modularmonolithstore.modules.cart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox.InMemoryInboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox.InboxMessageHandler;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleCommandDispatcher;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.event.UserRegisteredHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.command.CommandBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.domain.InMemoryCartRepository;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;
import io.vavr.control.Try;

public class CartModule implements ModuleCommandDispatcher {
    private final CommandBus commandBus;

    private CartModule(final CommandBus commandBus) {
        Preconditions.checkNotNull(commandBus, "Command bus can't be null");

        this.commandBus = commandBus;
    }

    public static CartModule initialize(MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var inboxRepository = new InMemoryInboxMessageRepository();
        var commandDispatcher = CommandBusFactory.create(new InMemoryCartRepository());

        messageBus.subscribe(
                UserRegisteredIntegrationEvent.class,
                new InboxMessageHandler<>(inboxRepository, new UserRegisteredHandler(commandDispatcher))
        );

        return new CartModule(commandDispatcher);
    }

    @Override
    public Try<Void> dispatchCommand(Command command) {
        return commandBus.dispatch(command);
    }
}
