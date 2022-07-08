package com.mateuszziomek.modularmonolithstore.modules.cart.application.command.createcart;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.command.CommandValidator;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

public class CreateCartValidator implements CommandValidator<CreateCartCommand> {
    private static final String CART_ID_ERROR = "Cart id is required";

    @Override
    public Validation<Seq<String>, CreateCartCommand> validate(final CreateCartCommand command) {
        return Validation
                .combine(
                        // @TODO handle validation of single field
                        validateCartId(command.cartId()),
                        validateCartId(command.cartId())
                )
                .ap((id1, id2) -> new CreateCartCommand(id1));
    }

    private Validation<String, UUID> validateCartId(final UUID cartId) {
        if (cartId == null) {
            return Validation.invalid(CART_ID_ERROR);
        }

        return Validation.valid(cartId);
    }
}
