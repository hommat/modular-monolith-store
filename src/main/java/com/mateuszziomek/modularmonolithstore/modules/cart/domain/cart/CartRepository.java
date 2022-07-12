package com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateRepository;

public interface CartRepository extends AggregateRepository<CartId, Cart> { }
