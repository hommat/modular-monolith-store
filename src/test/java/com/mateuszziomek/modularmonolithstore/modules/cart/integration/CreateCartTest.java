package com.mateuszziomek.modularmonolithstore.modules.cart.integration;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidationException;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.TestMessageBus;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;
import com.mateuszziomek.modularmonolithstore.modules.cart.CartModule;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails.GetDetailsCartQuery;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateCartTest {
    @Test
    void cartCanBeCreated() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = CartModule.bootstrap(messageBus);
        var uuid = UUID.randomUUID();

        // Act
        var result = sut.dispatchCommand(new CreateCartCommand(uuid));

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();

        StepVerifier
                .create(sut.dispatchQuery(new GetDetailsCartQuery(uuid)))
                .expectNextMatches(cart -> cart.id().equals(uuid))
                .verifyComplete();
    }

    @Test
    void cartIsCreatedOnUserRegisteredIntegrationEvent() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = CartModule.bootstrap(messageBus);
        var uuid = UUID.randomUUID();
        var event = new UserRegisteredIntegrationEvent(uuid, "username");

        // Act
        var result = messageBus.publish(event);

        // Assert
        StepVerifier
                .create(result)
                .verifyComplete();

        StepVerifier
                .create(sut.dispatchQuery(new GetDetailsCartQuery(uuid)))
                .expectNextMatches(cart -> cart.id().equals(uuid))
                .verifyComplete();
    }

    @Test
    void inputIsValidated() {
        var messageBus = new TestMessageBus();
        var sut = CartModule.bootstrap(messageBus);

        // Act
        var result = sut.dispatchCommand(new CreateCartCommand(null));

        // Assert
        StepVerifier
                .create(result)
                .verifyError(CommandValidationException.class);
    }
}
