package com.mateuszziomek.modularmonolithstore.modules.user.domain.user;

import com.mateuszziomek.modularmonolithstore.buildingblocks.domain.AggregateId;

import java.util.UUID;

public class UserId extends AggregateId {
    public UserId(final UUID value) {
        super(value);
    }
}
