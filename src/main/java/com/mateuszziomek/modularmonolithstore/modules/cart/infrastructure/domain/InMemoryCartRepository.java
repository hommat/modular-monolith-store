package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.domain;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.Cart;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartId;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;
import io.vavr.control.Option;

import java.util.HashMap;

public class InMemoryCartRepository implements CartRepository {
    private final HashMap<CartId, Cart> cartsById = new HashMap<>(10000);

    @Override
    public Option<Cart> findById(final CartId id) {
        Preconditions.checkNotNull(id, "Id can't be null");

        var cart = cartsById.get(id);

        return cart == null ? Option.none() : Option.of(cart);
    }

    @Override
    public void save(Cart cart) {
        Preconditions.checkNotNull(cart, "Cart can't be null");

        cartsById.put(cart.id(), cart);
        cart.markDomainEventsAsCommitted();
    }
}
