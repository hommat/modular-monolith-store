package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import io.vavr.control.Option;

public interface CartRepository {
    Option<Cart> findById(CartId id);
    void save(Cart user);
}
