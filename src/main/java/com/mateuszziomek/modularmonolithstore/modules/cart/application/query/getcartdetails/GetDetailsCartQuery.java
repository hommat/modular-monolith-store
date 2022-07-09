package com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;
import com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart.DetailsCart;
import io.vavr.control.Option;

import java.util.UUID;

public class GetDetailsCartQuery implements Query<Option<DetailsCart>> {
    private final UUID id;

    public GetDetailsCartQuery(final UUID id) {
        // @TODO add validator
        this.id = id;
    }

    public UUID id() {
        return id;
    }

    @Override
    public String toString() {
        return "GetDetailsCartQuery{" +
                "id=" + id +
                '}';
    }
}
