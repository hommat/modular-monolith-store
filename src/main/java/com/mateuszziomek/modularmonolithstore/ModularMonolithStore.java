package com.mateuszziomek.modularmonolithstore;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.eventbus.impl.InMemoryMessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.module.ModuleMessageProcessor;
import com.mateuszziomek.modularmonolithstore.modules.cart.CartModule;
import com.mateuszziomek.modularmonolithstore.modules.user.UserModule;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import io.vavr.collection.List;

import java.time.Duration;
import java.util.UUID;

public class ModularMonolithStore {
    public static void main(String[] args) {
        var messageBus = new InMemoryMessageBus();

        var userModule = UserModule.initialize(messageBus);
        var cartModule = CartModule.initialize(messageBus);

        var moduleMessageProcessor = new MessageProcessor(
                List.of(userModule),
                10,
                Duration.ofMillis(3000)
            );
        var messageProcessingThread = new Thread(moduleMessageProcessor);

        messageProcessingThread.start();

        var userId = UUID.randomUUID();
        var registerCommand = new RegisterCommand(userId, "username", "password");
        var changePasswordCommand = new ChangePasswordCommand(UUID.randomUUID(), "adasd");

        var registerResult = userModule.dispatchCommand(registerCommand).block();
        var registerResult2 = userModule.dispatchCommand(registerCommand).block();
        var changePasswordResult = userModule.dispatchCommand(changePasswordCommand).block();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var registerCommand3 = new RegisterCommand(userId, "username33", "password44");
        var registerResult3 = userModule.dispatchCommand(registerCommand3).block();
    }

    public static class MessageProcessor implements Runnable {
        private final List<ModuleMessageProcessor> moduleMessageProcessors;
        private final int messagesPerProcess;
        private final Duration messageProcessInterval;

        public MessageProcessor(
                final List<ModuleMessageProcessor> moduleMessageProcessors,
                final int messagesPerProcess,
                final Duration messageProcessInterval
        ) {
            Preconditions.checkNotNull(moduleMessageProcessors, "Message processors can't be null");
            Preconditions.checkNotNull(messageProcessInterval, "Interval can't be null");

            this.moduleMessageProcessors = moduleMessageProcessors;
            this.messagesPerProcess = messagesPerProcess;
            this.messageProcessInterval = messageProcessInterval;
        }

        @Override
        public void run() {
            while (true) {
                moduleMessageProcessors.forEach(module -> module.processMessages(messagesPerProcess));
                try {
                    // @TODO use something different than Thread.sleep
                    Thread.sleep(messageProcessInterval.toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
