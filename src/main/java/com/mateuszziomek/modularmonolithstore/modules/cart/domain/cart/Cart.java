package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateRoot;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.event.CartCreatedDomainEvent;

public class Cart extends AggregateRoot {
    private CartId cartId;

    public Cart() {
        // Required for event sourcing
    }

    public static Cart create(final CartId cartId) {
        Preconditions.checkNotNull(cartId, "Cart id can't be null");

        var cart = new Cart();
        cart.raiseEvent(new CartCreatedDomainEvent(cartId));

        return cart;
    }

    public CartId id() {
        return cartId;
    }

    public void on(CartCreatedDomainEvent event) {
        cartId = event.cartId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return Objects.equal(cartId, cart.cartId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cartId);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                '}';
    }
}
