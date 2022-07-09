package com.mateuszziomek.modularmonolithstore.modules.user.integration;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.InMemoryMessageBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.TestMessageBus;
import com.mateuszziomek.modularmonolithstore.modules.user.UserModule;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.changepassword.ChangePasswordCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserChangePasswordTest {
    private TestMessageBus messageBus;
    private UserModule sut;

    @BeforeEach
    void beforeEach() {
        messageBus = new TestMessageBus();
        sut = UserModule.initialize(messageBus);
    }

    @Test
    void userPasswordCanBeChanged() {
        // Arrange
        var uuid = UUID.randomUUID();
        sut.dispatchCommand(new RegisterCommand(uuid, "username", "password"));
        sut.processMessages(10);
        messageBus.clearPublishedMessages();

        // Act
        var result = sut.dispatchCommand(new ChangePasswordCommand(uuid, "new password"));
        sut.processMessages(10);

        // Assert
        assertThat(result.isSuccess()).isTrue();

        assertThat(messageBus.publishedMessages.length()).isZero();
    }

    @Test
    void userMustExists() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.initialize(new InMemoryMessageBus());

        // Act
        var result = sut.dispatchCommand(new ChangePasswordCommand(UUID.randomUUID(), "new password"));
        sut.processMessages(10);

        // Assert
        assertThat(result.isFailure()).isTrue();

        assertThat(messageBus.publishedMessages.length()).isZero();
    }
}
