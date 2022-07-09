package com.mateuszziomek.modularmonolithstore.modules.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxProcessor;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.CommandDispatcherModule;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.MessageProcessorModule;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.QueryDispatcherModule;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command.CommandBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.query.QueryBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.InMemoryUserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.TemporaryPasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox.OutboxMessageRepositoryFactory;
import io.vavr.control.Try;


public class UserModule implements CommandDispatcherModule, QueryDispatcherModule, MessageProcessorModule {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final OutboxProcessor outboxProcessor;

    private UserModule(
            final CommandBus commandBus,
            final QueryBus queryBus,
            final OutboxProcessor outboxProcessor
    ) {
        Preconditions.checkNotNull(commandBus, "Command bus can't be null");
        Preconditions.checkNotNull(queryBus, "Query bus can't be null");
        Preconditions.checkNotNull(outboxProcessor, "Outbox processor can't be null");

        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.outboxProcessor = outboxProcessor;
    }

    public static UserModule initialize(final MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var outboxMessageRepository = OutboxMessageRepositoryFactory.create();
        var userRepository = new InMemoryUserRepository(outboxMessageRepository);
        var commandBus = CommandBusFactory.create(
                userRepository,
                new TemporaryPasswordHashingAlgorithm()
        );
        var queryBus = QueryBusFactory.create(userRepository);
        var outboxProcessor = new OutboxProcessor(messageBus, outboxMessageRepository);

        return new UserModule(commandBus, queryBus, outboxProcessor);
    }

    @Override
    public Try<Void> dispatchCommand(final Command command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        return commandBus.dispatch(command);
    }

    @Override
    public void processMessages(final int amount) {
        outboxProcessor.process(amount);
    }

    @Override
    public <T> Try<T> dispatchQuery(final Query<T> query) {
        Preconditions.checkNotNull(query, "Query can't be null");

        return queryBus.dispatch(query);
    }
}
