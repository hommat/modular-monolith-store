package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.application.query;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryBus;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryLoggingDecorator;
import com.mateuszziomek.modularmonolithstore.modules.user.application.query.getdetailsuser.GetDetailsUserHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.application.query.getdetailsuser.GetDetailsUserQuery;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.UserRepository;
import com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details.DetailsUserFinder;

public class QueryBusFactory {
    private QueryBusFactory() {}

    public static QueryBus create(
        final UserRepository userRepository
    ) {
        Preconditions.checkNotNull(userRepository, "User repository can't be null");

        final var queryBus = new QueryBus();

        queryBus.registerQuery(
                GetDetailsUserQuery.class,
                new QueryLoggingDecorator<>(new GetDetailsUserHandler(new DetailsUserFinder(userRepository)))
        );

        return queryBus;
    }
}
