package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.event;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.DomainEvent;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartId;

public class CartCreatedDomainEvent extends DomainEvent {
    private final CartId cartId;

    public CartCreatedDomainEvent(final CartId cartId) {
        Preconditions.checkNotNull(cartId, "Cart id can't be null");

        this.cartId = cartId;
    }

    public CartId cartId() {
        return cartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartCreatedDomainEvent)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "CartCreatedDomainEvent{" +
                "cartId=" + cartId +
                ", id=" + id +
                ", occurAt=" + occurAt +
                '}';
    }
}
