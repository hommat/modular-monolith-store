package com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart.DetailsCart;
import com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart.DetailsCartFinder;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class GetDetailsCartHandler implements QueryHandler<Option<DetailsCart>, GetDetailsCartQuery> {
    private final DetailsCartFinder finder;

    public GetDetailsCartHandler(final DetailsCartFinder finder) {
        Preconditions.checkNotNull(finder, "Finder can't be null");

        this.finder = finder;
    }

    @Override
    public Try<Option<DetailsCart>> handle(final GetDetailsCartQuery query) {
        Preconditions.checkNotNull(query, "Query can't be null");

        return Try.success(finder.findById(query.id()));
    }
}
