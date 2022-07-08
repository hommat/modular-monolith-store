package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateId;

import java.util.UUID;

public class CartId extends AggregateId {
    public CartId(final UUID value) {
        super(value);
    }
}
