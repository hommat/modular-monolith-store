package com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartId;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class DetailsCartFinder {
    private final CartRepository cartRepository;

    public DetailsCartFinder(final CartRepository cartRepository) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        this.cartRepository = cartRepository;
    }

    public Mono<DetailsCart> findById(final UUID id) {
        Preconditions.checkNotNull(id, "Id can't be null");

        return cartRepository.findById(new CartId(id)).map(DetailsCartMapper::fromModel);
    }
}
