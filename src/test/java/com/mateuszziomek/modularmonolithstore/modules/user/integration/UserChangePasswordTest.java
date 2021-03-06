package com.mateuszziomek.modularmonolithstore.modules.user.integration;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidationException;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.InMemoryMessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.TestMessageBus;
import com.mateuszziomek.modularmonolithstore.modules.user.UserModule;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserChangePasswordTest {
    @Test
    void userPasswordCanBeChanged() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.bootstrap(messageBus);
        var uuid = UUID.randomUUID();
        sut.dispatchCommand(new RegisterCommand(uuid, "username", "password")).block();
        sut.processMessages(10).block();
        messageBus.clearPublishedMessages();

        // Act
        var result = sut.dispatchCommand(new ChangePasswordCommand(uuid, "new password"));

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();

        sut.processMessages(10).block();
        assertThat(messageBus.publishedMessages.length()).isZero();
    }

    @Test
    void userMustExists() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.bootstrap(new InMemoryMessageBus());

        // Act
        var result = sut.dispatchCommand(new ChangePasswordCommand(UUID.randomUUID(), "new password"));

        // Assert
        StepVerifier
                .create(result)
                .verifyError();

        sut.processMessages(10).block();
        assertThat(messageBus.publishedMessages.length()).isZero();
    }

    @Test
    void inputIsValidated() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.bootstrap(messageBus);
        sut.dispatchCommand(new RegisterCommand(UUID.randomUUID(), "username", "password")).block();

        // Act
        var result = sut.dispatchCommand(new ChangePasswordCommand(null, null));

        // Assert
        StepVerifier
                .create(result)
                .verifyError(CommandValidationException.class);
    }
}
