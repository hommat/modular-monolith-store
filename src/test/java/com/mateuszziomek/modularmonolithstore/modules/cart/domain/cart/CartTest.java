package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AbstractAggregateRootTest;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.event.CartCreatedDomainEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CartTest extends AbstractAggregateRootTest {
    @Test
    void cartCanBeCreated() {
        // Arrange
        var cartUUID = UUID.randomUUID();
        var cartId = new CartId(cartUUID);

        // Act
        var sut = Cart.create(cartId);

        // Assert
        assertThatNumberOfPendingEventsIs(sut, 1);

        var event = (CartCreatedDomainEvent) sut.pendingDomainEvents().get(0);
        assertThat(event).isInstanceOf(CartCreatedDomainEvent.class);
        assertThat(event.cartId()).isEqualTo(new CartId(cartUUID));
    }
}
