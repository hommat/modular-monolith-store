package com.mateuszziomek.modularmonolithstore.modules.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxProcessor;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleCommandDispatcher;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleMessageProcessor;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command.CommandBusFactory;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.InMemoryUserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.TemporaryPasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox.InMemoryOutboxMessageRepository;
import io.vavr.control.Try;

public class UserModule implements ModuleCommandDispatcher, ModuleMessageProcessor {
    private final CommandBus commandBus;
    private final OutboxProcessor outboxProcessor;

    private UserModule(final CommandBus commandBus, final OutboxProcessor outboxProcessor) {
        Preconditions.checkNotNull(commandBus, "Command dispatcher can't be null");
        Preconditions.checkNotNull(outboxProcessor, "Outbox processor can't be null");

        this.commandBus = commandBus;
        this.outboxProcessor = outboxProcessor;
    }

    public static UserModule initialize(final MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var outboxRepository = new InMemoryOutboxMessageRepository();
        var commandDispatcher = CommandBusFactory.create(
                new InMemoryUserRepository(outboxRepository),
                new TemporaryPasswordHashingAlgorithm()
        );
        var outboxProcessor = new OutboxProcessor(messageBus, outboxRepository);

        return new UserModule(commandDispatcher, outboxProcessor);
    }

    @Override
    public Try<Void> dispatchCommand(Command command) {
        return commandBus.dispatch(command);
    }

    @Override
    public void processMessages(int amount) {
        outboxProcessor.process(amount);
    }
}
