package com.mateuszziomek.modularmonolithstore.modules.user.integration;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidationException;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.TestMessageBus;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;
import com.mateuszziomek.modularmonolithstore.modules.user.UserModule;
import com.mateuszziomek.modularmonolithstore.modules.user.application.command.register.RegisterCommand;
import com.mateuszziomek.modularmonolithstore.modules.user.application.query.getdetailsuser.GetDetailsUserQuery;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserRegisterTest {
    @Test
    void userCanBeRegistered() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.bootstrap(messageBus);
        var uuid = UUID.randomUUID();

        // Act
        var result = sut.dispatchCommand(new RegisterCommand(uuid, "username", "password"));

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();

        StepVerifier
                .create(sut.dispatchQuery(new GetDetailsUserQuery(uuid)))
                .expectNextMatches(user -> user.id().equals(uuid))
                .verifyComplete();

        sut.processMessages(10).block();
        assertThat(messageBus.publishedMessages.length()).isEqualTo(1);
        var event = (UserRegisteredIntegrationEvent) messageBus.publishedMessages.get(0);
        assertThat(event).isInstanceOf(UserRegisteredIntegrationEvent.class);
        assertThat(event.userId()).isEqualTo(uuid);
        assertThat(event.username()).isEqualTo("username");
    }

    @Test
    void inputIsValidated() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = UserModule.bootstrap(messageBus);

        // Act
        var result = sut.dispatchCommand(new RegisterCommand(null, null, null));

        // Assert
        StepVerifier
                .create(result)
                .verifyError(CommandValidationException.class);
    }
}
