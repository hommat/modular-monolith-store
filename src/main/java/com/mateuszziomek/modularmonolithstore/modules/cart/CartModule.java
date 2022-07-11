package com.mateuszziomek.modularmonolithstore.modules.cart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.CommandDispatcherModule;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.QueryDispatcherModule;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.command.CommandBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.event.EventConfiguration;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.query.QueryBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.domain.InMemoryCartRepository;
import reactor.core.publisher.Mono;

public class CartModule implements CommandDispatcherModule, QueryDispatcherModule {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    private CartModule(final CommandBus commandBus, final QueryBus queryBus) {
        Preconditions.checkNotNull(commandBus, "Command bus can't be null");
        Preconditions.checkNotNull(queryBus, "Query bus can't be null");

        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    public static CartModule bootstrap(final MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var cartRepository = new InMemoryCartRepository();
        var commandBus = CommandBusFactory.create(cartRepository);
        var queryBus = QueryBusFactory.create(cartRepository);
        EventConfiguration.configure(messageBus, commandBus);

        return new CartModule(commandBus, queryBus);
    }

    @Override
    public Mono<Void> dispatchCommand(final Command command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        return commandBus.dispatch(command);
    }

    @Override
    public <T> T dispatchQuery(final Query<T> query) {
        Preconditions.checkNotNull(query, "Query can't be null");

        return queryBus.dispatch(query);
    }
}
