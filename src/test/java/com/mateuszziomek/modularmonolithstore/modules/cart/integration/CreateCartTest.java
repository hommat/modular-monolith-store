package com.mateuszziomek.modularmonolithstore.modules.cart.integration;

import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.message.TestMessageBus;
import com.mateuszziomek.modularmonolithstore.integration.event.UserRegisteredIntegrationEvent;
import com.mateuszziomek.modularmonolithstore.modules.cart.CartModule;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart.CreateCartCommand;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails.GetDetailsCartQuery;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateCartTest {
    @Test
    void cartCanBeCreated() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = CartModule.initialize(messageBus);
        var uuid = UUID.randomUUID();

        // Act
        var result = sut.dispatchCommand(new CreateCartCommand(uuid));

        // Assert
        assertThat(result.isSuccess()).isTrue();

        var getCartQueryResult = sut.dispatchQuery(new GetDetailsCartQuery(uuid));
        var cart = getCartQueryResult.get().get();
        assertThat(cart.id()).isEqualTo(uuid);
    }

    @Test
    void cartIsCreatedOnUserRegisteredIntegrationEvent() {
        // Arrange
        var messageBus = new TestMessageBus();
        var sut = CartModule.initialize(messageBus);
        var uuid = UUID.randomUUID();
        var event = new UserRegisteredIntegrationEvent(uuid, "username");

        // Act
        var result = messageBus.publish(event);

        // Assert
        assertThat(result.isSuccess()).isTrue();

        var getCartQueryResult = sut.dispatchQuery(new GetDetailsCartQuery(uuid));
        var cart = getCartQueryResult.get().get();
        assertThat(cart.id()).isEqualTo(uuid);
    }
}
