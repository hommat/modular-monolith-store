package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.domain;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.InMemoryAggregateRepository;
import com.mateuszziomek.modularmonolithstore.buildingblocks.infrastructure.outbox.OutboxMessageRepository;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.Cart;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartId;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;

public class InMemoryCartRepository extends InMemoryAggregateRepository<CartId, Cart> implements CartRepository {
    public InMemoryCartRepository(OutboxMessageRepository outboxMessageRepository) {
        super(outboxMessageRepository);
    }
}