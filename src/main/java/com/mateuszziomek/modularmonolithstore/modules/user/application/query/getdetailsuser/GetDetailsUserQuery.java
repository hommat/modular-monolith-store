package com.mateuszziomek.modularmonolithstore.modules.user.application.query.getdetailsuser;

import com.mateuszziomek.modularmonolithstore.buildingblocks.application.query.Query;
import com.mateuszziomek.modularmonolithstore.modules.user.readmodel.user.details.DetailsUser;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GetDetailsUserQuery implements Query<Mono<DetailsUser>> {
    private final UUID id;

    public GetDetailsUserQuery(final UUID id) {
        // @TODO add validator
        this.id = id;
    }

    public UUID id() {
        return id;
    }

    @Override
    public String toString() {
        return "GetDetailsUserQuery{" +
                "id=" + id +
                '}';
    }
}
