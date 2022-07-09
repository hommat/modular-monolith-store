package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.event.EventLoggingDecorator;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox.InMemoryInboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.inbox.InboxMessageHandler;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.event.UserRegisteredHandler;

public class EventConfiguration {
    private EventConfiguration() {}

    public static void configure(final MessageBus messageBus, final CommandBus commandBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");
        Preconditions.checkNotNull(commandBus, "Command bus can't be null");

        var inboxRepository = new InMemoryInboxMessageRepository();

        messageBus.subscribe(
                UserRegisteredIntegrationEvent.class,
                new InboxMessageHandler<>(inboxRepository, new EventLoggingDecorator<>(new UserRegisteredHandler(commandBus)))
        );
    }
}
