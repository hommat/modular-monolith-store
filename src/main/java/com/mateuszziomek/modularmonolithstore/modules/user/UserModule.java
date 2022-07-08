package com.mateuszziomek.modularmonolithstore.modules.user;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandDispatcher;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.MessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxProcessor;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleCommandDispatcher;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleMessageProcessor;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.command.CommandDispatcherFactory;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.InMemoryUserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user.TemporaryPasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.outbox.InMemoryOutboxMessageRepository;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

public class UserModule implements ModuleCommandDispatcher, ModuleMessageProcessor {
    private final CommandDispatcher commandDispatcher;
    private final OutboxProcessor outboxProcessor;

    private UserModule(final CommandDispatcher commandDispatcher, final OutboxProcessor outboxProcessor) {
        Preconditions.checkNotNull(commandDispatcher, "Command dispatcher can't be null");
        Preconditions.checkNotNull(outboxProcessor, "Outbox processor can't be null");

        this.commandDispatcher = commandDispatcher;
        this.outboxProcessor = outboxProcessor;
    }

    public static UserModule initialize(final MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus, "Message bus can't be null");

        var outboxRepository = new InMemoryOutboxMessageRepository();
        var commandDispatcher = CommandDispatcherFactory.create(
                new InMemoryUserRepository(outboxRepository),
                new TemporaryPasswordHashingAlgorithm()
        );
        var outboxProcessor = new OutboxProcessor(messageBus, outboxRepository);

        return new UserModule(commandDispatcher, outboxProcessor);
    }

    @Override
    public Mono<Try<Void>> dispatchCommand(Command command) {
        return commandDispatcher.dispatch(command);
    }

    @Override
    public void processMessages(int amount) {
        outboxProcessor.process(amount);
    }
}
