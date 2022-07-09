package com.mateuszziomek.modularmonolithstore.modules.cart.infrastructure.application.query;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryLoggingDecorator;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails.GetDetailsCartHandler;
import com.mateuszziomek.modularmonolithstore.modules.cart.application.query.getcartdetails.GetDetailsCartQuery;
import com.mateuszziomek.modularmonolithstore.modules.cart.domain.cart.CartRepository;
import com.mateuszziomek.modularmonolithstore.modules.cart.readmodel.cart.DetailsCartFinder;

public class QueryBusFactory {
    private QueryBusFactory() {}

    public static QueryBus create(
            final CartRepository cartRepository
    ) {
        Preconditions.checkNotNull(cartRepository, "Cart repository can't be null");

        final var queryBus = new QueryBus();

        queryBus.registerQuery(
                GetDetailsCartQuery.class,
                new QueryLoggingDecorator<>(new GetDetailsCartHandler(new DetailsCartFinder(cartRepository)))
        );

        return queryBus;
    }
}
