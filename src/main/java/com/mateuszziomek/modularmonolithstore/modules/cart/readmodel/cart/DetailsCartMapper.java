package com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.Cart;

public class DetailsCartMapper {
    private DetailsCartMapper() {}

    public static DetailsCart fromModel(final Cart cart) {
        Preconditions.checkNotNull(cart, "Cart can't be null");

        return DetailsCart
                .builder()
                .id(cart.id().value())
                .build();
    }
}
