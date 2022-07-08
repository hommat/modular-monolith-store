package com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.Command;

import java.util.UUID;

public class CreateCartCommand implements Command {
    private final UUID cartId;

    public CreateCartCommand(final UUID cartId) {
        this.cartId = cartId;
    }

    public UUID cartId() {
        return cartId;
    }
}
