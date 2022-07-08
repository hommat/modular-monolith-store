package com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.Cart;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartId;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;
import io.vavr.control.Try;

public class CreateCartHandler implements CommandHandler<CreateCartCommand> {
    private final CartRepository cartRepository;

    public CreateCartHandler(final CartRepository cartRepository) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        this.cartRepository = cartRepository;
    }

    @Override
    public Try<Void> handle(final CreateCartCommand command) {
        Preconditions.checkNotNull(command, "Command can't be null");

        final var cartId = new CartId(command.cartId());
        final var cart = Cart.create(cartId);

        cartRepository.save(cart);

        return Try.success(null);
    }
}
