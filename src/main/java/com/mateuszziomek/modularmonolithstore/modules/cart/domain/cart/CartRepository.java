package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import reactor.core.publisher.Mono;

public interface CartRepository {
    Mono<Cart> findById(CartId id);
    Mono<Void> save(Cart user);
}
