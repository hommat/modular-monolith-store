package com.mateuszziomek.modules.user.domain.user;

import com.mateuszziomek.buildingblocks.domain.AggregateId;

import java.util.UUID;

public class UserId extends AggregateId {
    public UserId(final UUID value) {
        super(value);
    }
}
