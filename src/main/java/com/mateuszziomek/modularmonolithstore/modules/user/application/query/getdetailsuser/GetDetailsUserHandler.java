package com.mateuszziomek.modularmonolithstore.modules.user.application.query.getdetailsuser;

import com.google.common.base.Preconditions;
import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.QueryHandler;
import com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details.DetailsUser;
import com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details.DetailsUserFinder;
import reactor.core.publisher.Mono;

public class GetDetailsUserHandler implements QueryHandler<Mono<DetailsUser>, GetDetailsUserQuery> {
    private final DetailsUserFinder finder;

    public GetDetailsUserHandler(final DetailsUserFinder finder) {
        Preconditions.checkNotNull(finder, "Finder can't be null");

        this.finder = finder;
    }

    @Override
    public Mono<DetailsUser> handle(final GetDetailsUserQuery query) {
        Preconditions.checkNotNull(query, "Query can't be null");

        return finder.findById(query.id());
    }
}
